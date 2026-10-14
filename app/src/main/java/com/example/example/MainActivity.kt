package com.example.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.example.ui.theme.ExampleTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExampleTheme {
                UILayoutDemoScreen()
            }
        }
    }
}


@Composable
fun UILayoutDemoScreen() {

    var nameInput by remember {
        mutableStateOf("")
    }

    var submittedName by remember {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ==============================
        // BOX DAN IMAGE
        // ==============================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.ic_launcher_foreground
                    ),
                    contentDescription = "Logo Android",
                    modifier = Modifier.size(60.dp)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Komponen di dalam Box",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }


        // ==============================
        // ROW
        // ==============================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BadgeCard(
                title = "Row 1",
                color = Color(0xFFE3F2FD)
            )

            BadgeCard(
                title = "Row 2",
                color = Color(0xFFE8F5E9)
            )

            BadgeCard(
                title = "Row 3",
                color = Color(0xFFFFF3E0)
            )
        }


        // ==============================
        // FORM INPUT
        // ==============================

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Latihan Input Data",
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = {
                        nameInput = it
                    },
                    label = {
                        Text("Masukkan Nama Mahasiswa")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {
                        submittedName = nameInput
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {

                    Text("Tampilkan")
                }

                if (submittedName.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Halo, $submittedName! Selamat Belajar Compose.",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


// ==============================
// BADGE CARD
// ==============================

@Composable
fun BadgeCard(
    title: String,
    color: Color
) {

    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(4.dp)
    ) {

        Text(
            text = title,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


// ==============================
// PREVIEW
// ==============================

@Preview(
    showBackground = true
)
@Composable
fun UILayoutDemoScreenPreview() {
    ContohTheme {
        UILayoutDemoScreen()
    }
}