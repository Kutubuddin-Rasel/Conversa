package com.example.chat.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat.R
import com.example.chat.Routes
import com.example.chat.util.toast
import com.example.chat.viewModel.UserDetailsViewModel

@Composable
fun Username(navController: NavController) {
    val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
    val username = userDetailsViewModel.username.collectAsState()
    val phoneNumber = userDetailsViewModel.phoneNumber.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context= LocalContext.current
    Column(modifier = Modifier.background(Color.White)) {
        Column(modifier = Modifier.fillMaxWidth().weight(0.3f)) {
            Image(
                painter = painterResource(R.drawable.start),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxWidth().background(Color.Transparent)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().weight(0.1f)) {
            Text(
                text = "Connect easily with\nyour family and friends\nover countries",
                textAlign = TextAlign.Center,
                color = Color(16, 29, 77),
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 36.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Column(modifier = Modifier.fillMaxWidth().weight(0.06f)) {
            OutlinedTextField(
                value = username.value,
                onValueChange = { userDetailsViewModel.setUsername(it) },
                placeholder = { Text("Username") },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // Set the action button to "Done"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Hide the keyboard when "Done" is pressed
                    }
                )
            )
        }
        Column(modifier = Modifier.fillMaxWidth().weight(0.06f)) {
            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = { userDetailsViewModel.setPhoneNumber(it) },
                placeholder = { Text("Phone Number") },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, // Set the action button to "Done"
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Hide the keyboard when "Done" is pressed
                    }
                )
            )
        }
        Column(modifier = Modifier.fillMaxWidth().weight(0.1f)) {
            Button(
                onClick = {
                    if(check(username.value,phoneNumber.value,context)){
                        userDetailsViewModel.SaveUser(username.value,phoneNumber.value){
                            navController.navigate(Routes.searchUser)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(16, 29, 77)),
                modifier = Modifier.size(290.dp, 50.dp).align(Alignment.CenterHorizontally)
            ) {
                Text("Start Messaging", fontSize = 18.sp)
            }
        }
    }
}
fun check(username:String,phonenumber:String,context:Context):Boolean{
    val value: Boolean
    if(username.isEmpty()){ toast(context,"Username can't be empty") }
    if(phonenumber.isEmpty()){ toast(context,"Phone Number can't be empty") }
    if(username.isEmpty()||phonenumber.isEmpty()){
        toast(context,"Enter Username and Phone Number")
        value=false
    }
    else{
        value=true
    }
    return value
}