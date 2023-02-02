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
            DefaultPreview()
            }
        }
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

@Composable
fun switchRow(type: String,id_true:Int,id_fault:Int){
    Row(
        modifier = Modifier
        .background(Color.LightGray)
    ){
        var Boolean by remember {
            mutableStateOf(true)
        }
        textState(type = type, state = Boolean)
        imgState(state = Boolean, id_true =id_true , id_fault = id_fault)
        Switch(checked = Boolean, onCheckedChange = {Boolean = !Boolean})
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
        switchRow("Lamp",R.drawable.fat_yoshi,R.drawable.yoshi)
        switchRow("Door",R.drawable.fat_yoshi,R.drawable.yoshi)
        switchRow("Windows",R.drawable.fat_yoshi,R.drawable.yoshi)
    }
}