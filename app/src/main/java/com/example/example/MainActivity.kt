package com.example.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.example.ui.theme.ExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menyembunyikan status bar
        val controller = WindowCompat.getInsetsController(
            window,
            window.decorView
        )

        controller.hide(WindowInsetsCompat.Type.statusBars())

        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            ExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BukuCatatanScreen()
                }
            }
        }
    }
}

@Composable
fun BukuCatatanScreen() {

    val context = LocalContext.current

    // Mengambil DAO dari database
    val dao = remember {
        AppDatabase.getDatabase(context).bukuCatatanDao()
    }

    val scope = rememberCoroutineScope()

    // ---------- STATE FORM ----------

    var judul by rememberSaveable {
        mutableStateOf("")
    }

    var kategori by rememberSaveable {
        mutableStateOf("")
    }

    var isi by rememberSaveable {
        mutableStateOf("")
    }

    // ---------- STATE ERROR ----------

    var judulError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var kategoriError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var isiError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // null = mode tambah, berisi ID = mode edit
    var idEdit by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    // null = dialog tertutup, berisi ID = dialog hapus
    var idHapus by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    // ---------- STATE DAFTAR CATATAN ----------

    val daftarCatatan = remember {
        mutableStateListOf<BukuCatatanEntity>()
    }

    // Membaca ulang data dari database
    suspend fun muatUlang() {

        val data = withContext(Dispatchers.IO) {
            dao.getAllCatatan()
        }

        daftarCatatan.clear()
        daftarCatatan.addAll(data)
    }

    // Mengosongkan form
    fun resetForm() {

        judul = ""
        kategori = ""
        isi = ""

        judulError = null
        kategoriError = null
        isiError = null

        idEdit = null
    }

    // Membaca data saat halaman pertama kali dibuka
    LaunchedEffect(Unit) {
        muatUlang()
    }

    // ---------- DIALOG KONFIRMASI HAPUS ----------

    idHapus?.let { id ->

        val target = daftarCatatan.firstOrNull {
            it.id == id
        }

        AlertDialog(
            onDismissRequest = {
                idHapus = null
            },

            title = {
                Text("Konfirmasi Hapus")
            },

            text = {
                Text(
                    "Yakin ingin menghapus catatan \"${target?.judul ?: ""}\"?"
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {

                        idHapus = null

                        scope.launch {

                            withContext(Dispatchers.IO) {
                                dao.deleteById(id)
                            }

                            if (idEdit == id) {
                                resetForm()
                            }

                            muatUlang()

                            Toast.makeText(
                                context,
                                "Catatan dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text(
                        "Ya, Hapus",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        idHapus = null
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    // ---------- TAMPILAN UTAMA ----------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = if (idEdit == null) {
                "Tambah Catatan"
            } else {
                "Edit Catatan"
            },
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider()

        // INPUT JUDUL
        OutlinedTextField(
            value = judul,
            onValueChange = {
                judul = it
                judulError = null
            },
            label = {
                Text("Judul")
            },
            isError = judulError != null,
            supportingText = {
                judulError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // INPUT KATEGORI
        OutlinedTextField(
            value = kategori,
            onValueChange = {
                kategori = it
                kategoriError = null
            },
            label = {
                Text("Kategori (mis. Kuliah, Pribadi)")
            },
            isError = kategoriError != null,
            supportingText = {
                kategoriError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // INPUT ISI CATATAN
        OutlinedTextField(
            value = isi,
            onValueChange = {
                isi = it
                isiError = null
            },
            label = {
                Text("Isi Catatan")
            },
            minLines = 2,
            isError = isiError != null,
            supportingText = {
                isiError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // ---------- TOMBOL SIMPAN / UPDATE ----------

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {

                    // VALIDASI FORM
                    var isValid = true

                    if (judul.isBlank()) {
                        judulError = "Judul wajib diisi!"
                        isValid = false
                    }

                    if (kategori.isBlank()) {
                        kategoriError = "Kategori wajib diisi!"
                        isValid = false
                    }

                    if (isi.isBlank()) {
                        isiError = "Isi catatan wajib diisi!"
                        isValid = false
                    }

                    if (!isValid) {
                        return@Button
                    }

                    // Menyalin nilai sebelum coroutine
                    val sedangEdit = idEdit
                    val judulFinal = judul.trim()
                    val kategoriFinal = kategori.trim()
                    val isiFinal = isi.trim()

                    val tanggalLama = daftarCatatan
                        .firstOrNull {
                            it.id == sedangEdit
                        }?.tanggalDibuat

                    val tanggalBaru = SimpleDateFormat(
                        "dd MMM yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date())

                    // CREATE ATAU UPDATE
                    scope.launch {

                        withContext(Dispatchers.IO) {

                            if (sedangEdit == null) {

                                // CREATE
                                dao.insertCatatan(
                                    BukuCatatanEntity(
                                        judul = judulFinal,
                                        isi = isiFinal,
                                        kategori = kategoriFinal,
                                        tanggalDibuat = tanggalBaru
                                    )
                                )

                            } else {

                                // UPDATE
                                dao.updateCatatan(
                                    BukuCatatanEntity(
                                        id = sedangEdit,
                                        judul = judulFinal,
                                        isi = isiFinal,
                                        kategori = kategoriFinal,
                                        tanggalDibuat = tanggalLama ?: tanggalBaru
                                    )
                                )
                            }
                        }

                        // REFRESH DAFTAR
                        muatUlang()

                        // RESET FORM
                        resetForm()

                        Toast.makeText(
                            context,
                            if (sedangEdit == null) {
                                "Catatan tersimpan"
                            } else {
                                "Catatan diperbarui"
                            },
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    if (idEdit == null) {
                        "Simpan Catatan"
                    } else {
                        "Perbarui Catatan"
                    }
                )
            }

            // TOMBOL BATAL SAAT EDIT
            if (idEdit != null) {

                OutlinedButton(
                    onClick = {
                        resetForm()
                    }
                ) {
                    Text("Batal")
                }
            }
        }

        HorizontalDivider()

        // ---------- DAFTAR CATATAN ----------

        Text(
            text = "Daftar Catatan (${daftarCatatan.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        if (daftarCatatan.isEmpty()) {

            Text(
                text = "Belum ada catatan",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = daftarCatatan,
                    key = { it.id }
                ) { catatan ->

                    CatatanCard(
                        catatan = catatan,

                        onEdit = {

                            // Mengisi form dengan data yang dipilih
                            judul = catatan.judul
                            kategori = catatan.kategori
                            isi = catatan.isi

                            judulError = null
                            kategoriError = null
                            isiError = null

                            idEdit = catatan.id
                        },

                        onDelete = {
                            idHapus = catatan.id
                        }
                    )
                }
            }
        }
    }
}

// ---------- KARTU CATATAN ----------

@Composable
fun CatatanCard(
    catatan: BukuCatatanEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = catatan.judul,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "${catatan.kategori} • ${catatan.tanggalDibuat}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = catatan.isi,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onEdit
                ) {
                    Text("Edit")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text(
                        "Hapus",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// ---------- PREVIEW ----------

@Preview(
    showBackground = true,
    name = "Preview Buku Catatan"
)
@Composable
fun BukuCatatanScreenPreview() {

    ExampleTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            BukuCatatanScreen()
        }
    }
}