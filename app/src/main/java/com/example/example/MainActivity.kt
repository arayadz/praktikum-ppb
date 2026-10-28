package com.example.example

import android.os.Bundle
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
        setContent {
            ExampleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BukuCatatanScreen()
                }
            }
        }
    }
}

@Composable
fun BukuCatatanScreen() {
    val context = LocalContext.current
    val dao = remember {
        AppDatabase.getDatabase(context).bukuCatatanDao()
    }
    val scope = rememberCoroutineScope()

    var judul by rememberSaveable { mutableStateOf("") }
    var kategori by rememberSaveable { mutableStateOf("") }
    var isi by rememberSaveable { mutableStateOf("") }

    var judulError by rememberSaveable { mutableStateOf<String?>(null) }
    var kategoriError by rememberSaveable { mutableStateOf<String?>(null) }
    var isiError by rememberSaveable { mutableStateOf<String?>(null) }

    val daftarCatatan = remember {
        mutableStateListOf<BukuCatatanEntity>()
    }

    LaunchedEffect(Unit) {
        val data = withContext(Dispatchers.IO) {
            dao.getAllCatatan()
        }
        daftarCatatan.clear()
        daftarCatatan.addAll(data)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Buku Catatan (Room Database)",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider()

        OutlinedTextField(
            value = judul,
            onValueChange = {
                judul = it
                judulError = null
            },
            label = { Text("Judul") },
            isError = judulError != null,
            supportingText = {
                judulError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

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
                kategoriError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = isi,
            onValueChange = {
                isi = it
                isiError = null
            },
            label = { Text("Isi Catatan") },
            minLines = 2,
            isError = isiError != null,
            supportingText = {
                isiError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
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

                if (isValid) {
                    val tanggal = SimpleDateFormat(
                        "dd MMM yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date())

                    val dataBaru = BukuCatatanEntity(
                        judul = judul.trim(),
                        isi = isi.trim(),
                        kategori = kategori.trim(),
                        tanggalDibuat = tanggal
                    )

                    scope.launch(Dispatchers.IO) {
                        dao.insertCatatan(dataBaru)
                        val listData = dao.getAllCatatan()

                        withContext(Dispatchers.Main) {
                            daftarCatatan.clear()
                            daftarCatatan.addAll(listData)
                            judul = ""
                            kategori = ""
                            isi = ""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Catatan")
        }

        HorizontalDivider()

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
                    daftarCatatan,
                    key = { it.id }
                ) { catatan ->
                    CatatanCard(catatan)
                }
            }
        }
    }
}

@Composable
fun CatatanCard(catatan: BukuCatatanEntity) {
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = catatan.isi,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true, name = "Preview Buku Catatan")
@Composable
fun BukuCatatanScreenPreview() {
    ExampleTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BukuCatatanScreen()
        }
    }
}
