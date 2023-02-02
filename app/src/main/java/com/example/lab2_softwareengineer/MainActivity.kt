package com.example.lab2_softwareengineer

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab2_softwareengineer.ui.theme.Lab2_SoftwareEngineerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            }
        }
    }

@Composable

fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun GoodBye(name: String){
    Text(text=" Good bye $name!")
}

@Composable
fun textState(type:String,state:Boolean){
    if (state) {
        Text(text = "$type : On")
    }
    else
        Text(text = "$type : Off")
}
@Composable
fun imgState(state:Boolean,id_true:Int,id_fault:Int){
    if(state){
        Image(
            painter = painterResource(id_true),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
        )
    }
    else{
        Image(
            painter = painterResource(id_fault),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
        ) {
            var lampState by remember {
                mutableStateOf(true)
            }
            textState("Lamp",lampState)
            imgState(state = lampState, id_true = R.drawable.fat_yoshi, id_fault = R.drawable.yoshi)
            Switch(checked = lampState, onCheckedChange = { lampState = !lampState })
        }
        Row() {
            var doorState by remember {
                mutableStateOf(true)
            }
            textState("Door",doorState)
            imgState(state = doorState, id_true = R.drawable.fat_yoshi, id_fault = R.drawable.yoshi)
            Switch(checked = doorState, onCheckedChange = { doorState = !doorState })
        }


        Row() {
            var windowState by remember {
                mutableStateOf(true)
            }
            textState("Door",windowState)
            imgState(state = windowState, id_true = R.drawable.fat_yoshi, id_fault = R.drawable.yoshi)
            Switch(checked = windowState, onCheckedChange = { windowState = !windowState })
        }
    }
}