
package com.example

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.example.ui.theme.ExampleTheme
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FormulirBeasiswaScreen()
                }
            }
        }
    }
}

@Composable
fun FormulirBeasiswaScreen() {

    var nama by rememberSaveable {
        mutableStateOf("")
    }

    var nim by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var ipk by rememberSaveable {
        mutableStateOf("")
    }

    var namaError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var nimError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var emailError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var ipkError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var statusMessage by rememberSaveable {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Formulir Pendaftaran Beasiswa",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider()

        // INPUT NAMA
        OutlinedTextField(
            value = nama,
            onValueChange = {
                nama = it
                namaError = null
            },
            label = {
                Text("Nama Lengkap")
            },
            isError = namaError != null,
            supportingText = {
                namaError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // INPUT NIM
        OutlinedTextField(
            value = nim,
            onValueChange = {
                nim = it
                nimError = null
            },
            label = {
                Text("NIM")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = nimError != null,
            supportingText = {
                nimError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // INPUT EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = {
                Text("Email Mahasiswa")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // INPUT IPK
        OutlinedTextField(
            value = ipk,
            onValueChange = {
                ipk = it
                ipkError = null
            },
            label = {
                Text("IPK")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            isError = ipkError != null,
            supportingText = {
                ipkError?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // TOMBOL SUBMIT
        Button(
            onClick = {

                var isValid = true

                // VALIDASI NAMA
                if (nama.isBlank()) {
                    namaError = "Nama wajib diisi!"
                    isValid = false
                }

                // VALIDASI NIM: 8-10 DIGIT
                if (!nim.matches(Regex("\\d{8,10}"))) {
                    nimError = "NIM harus terdiri dari 8 hingga 10 digit!"
                    isValid = false
                }

                // VALIDASI EMAIL
                if (!Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches()
                ) {
                    emailError = "Format email tidak valid!"
                    isValid = false
                }

                // VALIDASI IPK: 0.00-4.00
                val ipkValue = ipk.replace(",", ".")
                    .toDoubleOrNull()

                if (
                    ipkValue == null ||
                    !ipkValue.isFinite() ||
                    ipkValue < 0.0 ||
                    ipkValue > 4.0
                ) {
                    ipkError =
                        "IPK harus berupa angka antara 0.00 - 4.00!"
                    isValid = false
                }

                // OUTPUT JIKA VALIDASI BERHASIL
                if (isValid) {
                    statusMessage =
                        "Pendaftaran Berhasil Disimpan!\n\n" +
                                "Nama: $nama\n" +
                                "NIM: $nim\n" +
                                "Email: $email\n" +
                                "IPK: $ipkValue"
                } else {
                    statusMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Pendaftaran")
        }

        // HASIL PENDAFTARAN
        if (statusMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = statusMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Preview Formulir Beasiswa"
)
@Composable
fun FormulirBeasiswaScreenPreview() {

    ExampleTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            FormulirBeasiswaScreen()
        }
    }
}
