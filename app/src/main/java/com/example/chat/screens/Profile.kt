package com.example.chat.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chat.R
import com.example.chat.Routes
import com.example.chat.util.AuthState
import com.example.chat.util.toast
import com.example.chat.viewModel.AuthenticationViewModel
import com.example.chat.viewModel.ProfileViewModel


@Composable
fun Profile(navController:NavController) {
    val profileViewModel:ProfileViewModel= hiltViewModel()
    val authenticationViewModel:AuthenticationViewModel= hiltViewModel()
    val authstate=authenticationViewModel.authstate.collectAsState()
    val usernmae=profileViewModel.username.collectAsState()
    val phoneNumber=profileViewModel.phoneNumber.collectAsState()
    val selectedImageUri=profileViewModel.selectedImageUri.collectAsState()
    LaunchedEffect(authstate.value) {
        when(authstate.value){
            is AuthState.UnAuthenticated-> navController.navigate(Routes.login)
            else->Unit
        }
    }
    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                profileViewModel.setUri(uri)
            }
        }
    )
    val context= LocalContext.current
    val keyboardController= LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxSize()
        .paint(painter = painterResource( R.drawable.background), contentScale = ContentScale.Crop)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(.3f)
        ) {
            AsyncImage(
                model = if(selectedImageUri.value==null){
                    R.drawable.name
                } else {
                    selectedImageUri.value
                },
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .clickable {
                        singlePhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ,
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier.weight(.6f).background(Color.White, shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)).fillMaxSize(), contentAlignment = Alignment.Center
        ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
            OutlinedTextField(
                value =usernmae.value,
                onValueChange = { profileViewModel.setUsername(it) },
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

            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { profileViewModel.setPhoneNumber(it) },
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
            Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                Button(
                    onClick = {
                        profileViewModel.updateProfile(
                            selectedImageUri.value,
                            usernmae.value,
                            phoneNumber.value
                        ){
                            if(it){
                                toast(context,"Profile Updated")
                            }
                        }
                        keyboardController?.hide()
                    },
                    colors = ButtonDefaults.buttonColors(Color(16, 29, 77)),
                    modifier = Modifier.size(290.dp, 50.dp).align(Alignment.CenterHorizontally)
                ) {
                    Text("Update Profile", fontSize = 18.sp)
                }
                Spacer(Modifier.padding(bottom = 10.dp))
                Button(
                    onClick = { authenticationViewModel.logOut() },
                    colors = ButtonDefaults.buttonColors(Color(16, 29, 77)),
                    modifier = Modifier.size(290.dp, 50.dp).align(Alignment.CenterHorizontally)
                ) {
                    Text("Log out", fontSize = 18.sp)
                }
            }
            }
        }
    }
}
