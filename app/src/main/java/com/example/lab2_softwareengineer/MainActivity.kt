package com.example.lab2_softwareengineer

import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
        setStateOfDevice(false,"light")
        setStateOfDevice(false,"door")
        setStateOfDevice(false,"window")
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center

            ) {
                switchRow("light", R.drawable.light_on, R.drawable.light_off)
                Spacer(Modifier.size(20.dp))
                switchRow("door", R.drawable.door_open, R.drawable.door_closed)
                Spacer(Modifier.size(20.dp))
                switchRow("window", R.drawable.windows_open, R.drawable.windows_closed)

            }

        }
    }

    private fun addPostEventListener(db: DatabaseReference) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                lightState = dataSnapshot.child("light").value as String
                windowState = dataSnapshot.child("window").value as String
                doorState = dataSnapshot.child("door").value as String
                Log.e("States", "Light: $lightState Windows: $windowState Door: $doorState")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("addPostEventListener", "loadPost:onCancelled", databaseError.toException())
            }
        }
        db.addValueEventListener(postListener)
    }

    fun setStateOfDevice(state: Boolean, id : String) {
        val status = if (id == "light" && state)
            "On"
        else if (id == "light" && !state)
            "Off"
        else if (state)
            "Open"
        else "Closed"

        database.child(id).setValue(status)
    }




    @Composable

    fun switchRow(type: String,id_true:Int,id_fault:Int){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .border(2.dp, Color.Magenta)
                .padding(10.dp)
        ){
            var Boolean by remember {
                mutableStateOf(false)
            }
            imgState(state = Boolean, id_true =id_true , id_fault = id_fault)
            Spacer(Modifier.weight(1f))
            textState(type = type, state = Boolean)
            Spacer(Modifier.weight(1f))
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

    val status = if (type == "light" && state)
        "On"
    else if (type == "light" && !state)
        "Off"
    else if (state)
        "Open"
    else "Closed"


    Text(text = "$type: $status")
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

