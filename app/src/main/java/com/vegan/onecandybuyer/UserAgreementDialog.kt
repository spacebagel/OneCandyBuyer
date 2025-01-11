package com.vegan.onecandybuyer

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class UserAgreement {
    @Composable
    fun ShowAlert(onDismiss: () -> Unit, onNoClick: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Отказ от ответственности") },
            text = { Text("Используя это приложение, вы подтверждаете, что взвесили товар на весах в торговом зале Ашан, и указали истинные значения веса и цифрового кода.") },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text("Подтверждаю")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onNoClick()
                }) {
                    Text("Не подтверждаю")
                }
            }
        )
    }
}