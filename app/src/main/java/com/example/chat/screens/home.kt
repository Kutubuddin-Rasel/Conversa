package com.example.chat.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chat.R
import com.example.chat.Routes
import com.example.chat.util.AuthState
import com.example.chat.util.formatTimestampTotime
import com.example.chat.util.toast
import com.example.chat.viewModel.AuthenticationViewModel
import com.example.chat.viewModel.ProfileViewModel
import dagger.Provides

@Preview(showSystemUi = true)
@Composable
fun Profil() {
//    val profileViewModel: ProfileViewModel = hiltViewModel()
//    val usernmae=profileViewModel.username.collectAsState()
//    val phoneNumber=profileViewModel.phoneNumber.collectAsState()
//    val selectedImageUri=profileViewModel.selectedImageUri.collectAsState()
//    val singlePhotoLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri ->
//            if (uri != null) {
//                profileViewModel.setUri(uri)
//            }
//        }
//    )
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
                model = R.drawable.profile,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier
                    .background(Color.Black)
                    .clip(CircleShape)
                    .size(200.dp)
                    .clickable {
                        //singlePhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ,
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier.weight(.6f).background(Color.White, shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)).fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, bottom = 20.dp)) {
            Column() {
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                    ,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.name), contentDescription = null,
//                        tint = Color.Blue,
                        modifier = Modifier.size(25.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 10.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Name",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            ), modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value ="usernmae.value",
                            onValueChange = {  },
                            placeholder = { Text("Username") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done // Set the action button to "Done"
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide() // Hide the keyboard when "Done" is pressed
                                }
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This is not your username or pin. This name will \nbe visible to your friends",
                            maxLines = 2,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.LightGray)
                        )
                    }

                }
            }
                Spacer(modifier = Modifier.height(15.dp))
                Column() {
                    Row(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                        ,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.phone), contentDescription = null,
//                        tint = Color.Blue,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 10.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Phone",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                ), modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "0000000",
                                maxLines = 1,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(16,29,77)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                        Column {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color(17,29,77))
                        }
                    }

                }
            }
        }
    }
}
