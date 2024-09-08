package com.example.currencyconversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyconversion.ui.theme.CurrencyConversionTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConversionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CurrencyConverterUI(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyConverterUI(modifier: Modifier = Modifier) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("") }
    var toCurrency by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Result will appear here") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Enter amount") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = fromCurrency,
            onValueChange = { fromCurrency = it },
            label = { Text("From currency (e.g., USD)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = toCurrency,
            onValueChange = { toCurrency = it },
            label = { Text("To currency (e.g., EUR)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        Button(onClick = {
            convertCurrency(amount, fromCurrency, toCurrency) { convertedAmount ->
                result = convertedAmount
            }
        }) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(result)
    }
}

private fun convertCurrency(amount: String, fromCurrency: String, toCurrency: String, onResult: (String) -> Unit) {
    val amountDouble = amount.toDoubleOrNull()
    if (amountDouble != null && fromCurrency.isNotEmpty() && toCurrency.isNotEmpty()) {
        RetrofitClient.retrofitService.getConvertedCurrency(
            apiKey = "d15e4ba5e1b303ee4c7c848646c1331f890ba92b",
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            amount = amountDouble
        ).enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                if (response.isSuccessful) {
                    val convertedAmount = response.body()?.rates?.get(toCurrency)?.rate_for_amount
                    onResult("Converted amount: $convertedAmount")
                } else {
                    onResult("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                onResult("Failed: ${t.message}")
            }
        })
    } else {
        onResult("Please enter valid input")
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterUIPreview() {
    CurrencyConversionTheme {
        CurrencyConverterUI()
    }
}
