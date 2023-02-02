package com.example.lab2_softwareengineer

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var lightState: String
    private lateinit var windowState: String
    private lateinit var doorState: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
        addPostEventListener(database)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
                    .padding(16.dp)
            ) {
                switchRow("light",R.drawable.fat_yoshi,R.drawable.yoshi)
                switchRow("door",R.drawable.fat_yoshi,R.drawable.yoshi)
                switchRow("window",R.drawable.fat_yoshi,R.drawable.yoshi)
            }

        }
    }

    private fun addPostEventListener(db: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                lightState = dataSnapshot.child("light").value as String
                windowState = dataSnapshot.child("window").value as String
                doorState = dataSnapshot.child("door").value as String
                Log.e("tag", "$lightState $windowState $doorState")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("addPostEventListener", "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addValueEventListener(postListener)
    }

    fun setStateOfDevice(state: Boolean, id : String) {
        if (state){
            database.child(id).setValue("on")
        }
        else
            database.child(id).setValue("off")


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
            Switch(checked = Boolean,
                onCheckedChange = {
                    Boolean = !Boolean
                    setStateOfDevice(Boolean, type)
                })
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