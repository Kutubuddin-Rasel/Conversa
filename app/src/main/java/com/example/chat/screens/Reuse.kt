package com.example.chat.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat.R
import com.example.chat.Routes
import com.example.chat.util.AuthState
import com.example.chat.util.Authentication
import com.example.chat.util.toast
import com.example.chat.viewModel.AuthenticationViewModel
import com.example.chat.viewModel.SingInUpViewModel

@Composable
fun Reuse(navController: NavController, state: String) {
    val signInUpViewModel: SingInUpViewModel = hiltViewModel()
    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
    val authState = authenticationViewModel.authstate.collectAsState()
    val email = signInUpViewModel.email.collectAsState()
    val password = signInUpViewModel.password.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Autheticated->navController.navigate(Routes.chats)
            is AuthState.error -> toast(context, (authState.value as AuthState.error).message)
            else->Unit
        }
    }
Column {
    Box(
        modifier = Modifier
            .weight(.9f)
            .fillMaxWidth().background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
            // .padding(bottom = 100.dp) // Ensure there's enough space for the image at the bottom
        ) {
            OutlinedTextField(
                value = email.value,
                onValueChange = { signInUpViewModel.setEmail(it) },
                placeholder = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { signInUpViewModel.setPassword(it) },
                placeholder = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (state == Routes.login) {
                button(
                    "Log In", navController,
                    "Don't have an Account? Sign In", Routes.signIn,
                    email.value, password.value, context, state, authenticationViewModel
                )
            }
            if (state == Routes.signIn) {
                button(
                    "Sign In", navController,
                    "Already have an Account? Log In", Routes.login,
                    email.value, password.value, context, state, authenticationViewModel
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth().weight(.1f).background(Color.White)
    ){
        Image(
            painter = painterResource(R.drawable.wave),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(Color.Transparent)
        )
    }
}
}


@Composable
fun button(
    buttontext: String,
    navController: NavController,
    text: String,
    changestate: String,
    email: String,
    password: String,
    context: Context,
    state: String,
    authenticationViewModel: AuthenticationViewModel
){
    Button(
        onClick = {
            if(Authentication(email,password,context)) {
                if (state == Routes.login) {
                  authenticationViewModel.logIn(email,password){
                      if(it){
                          navController.navigate(Routes.chats)
                      }
                  }
                }
                if (state == Routes.signIn) {
                    authenticationViewModel.signIn(email,password){
                        if(it){
                            navController.navigate(Routes.usernameUi)
                        }
                    }
                }
            }
        },
        colors = ButtonDefaults.buttonColors(Color(16, 29, 77)),
        modifier = Modifier.size(290.dp, 50.dp)
    ) {
        Text(buttontext, fontSize = 18.sp)
    }
    TextButton(
        onClick = {
            navController.navigate(changestate)
        }
    ) {
        Text(text, fontSize = 17.sp, modifier = Modifier
            .padding(start = 15.dp, top = 10.dp)
        )
    }
}