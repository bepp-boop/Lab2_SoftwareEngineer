package com.example.lab2_softwareengineer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab2_softwareengineer.ui.theme.Lab2_SoftwareEngineerTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MainActivity : ComponentActivity() {
    var outputTxt by mutableStateOf("Click button for Speech text ")
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
                switchRow("light", R.drawable.fat_yoshi, R.drawable.yoshi)
                switchRow("door", R.drawable.fat_yoshi, R.drawable.yoshi)
                switchRow("window", R.drawable.fat_yoshi, R.drawable.yoshi)
                SpeechToText()

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
    fun switchRow(type: String, id_true: Int, id_fault: Int) {
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

   

    @Composable
    fun SpeechToText() {
        // on below line we are creating
        // variable for the context
        val context = LocalContext.current
        // on below line we are creating ui
        // for our home screen in column
        Column(
            // on below line we are specifying
            // modifier as max size for our column
            modifier = Modifier.fillMaxSize(),

            // on below line we are specifying
            // horizontal and vertical arrangement
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // on the below line we are creating a simple
            // text for displaying the heading.
            Text(
                text = "Speech to Text Example",

                // for this text we are specifying
                // style on below line
                style = MaterialTheme.typography.h6,

                // on below line we are specifying the
                // modifier and padding for our text
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),

                // on below line we are
                // specifying the text alignment.
                textAlign = TextAlign.Center
            )
            // on below line we are specifying
            // the spacer between two views.
            Spacer(modifier = Modifier.height(30.dp))

            // on below line we are creating button for our mic
            Button(
                // on below line we are specifying
                // the elevation for our button.
                elevation = ButtonDefaults.elevation(
                    // we are specifying elevation for different state of buttons.
                    defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp
                ),

                // on below line we are specifying the color for our button
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),

                // on below line we are specifying on click listener for our button
                onClick = { getSpeechInput(context = context) },
            ) {
                // on below line we are specifying
                // the icon for our button,
                Icon(
                    // in this we are specifying
                    // the icon as mic icon.
                    imageVector = Icons.Filled.Phone,

                    // on below line we are specifying
                    // content description as mic.
                    contentDescription = "Mic",

                    // on below line we are specifying
                    // tint color for our icon
                    tint = Color.Green,

                    // on below line we are specifying padding,
                    // height and width for our icon
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(5.dp)
                )
            }
            // again we are adding a spacer between two views.
            Spacer(modifier = Modifier.height(30.dp))
            // on below line we are creating a text
            // for displaying the speech to text output.
            Text(
                // on below line we are setting
                // output text in our text view
                text = outputTxt,

                // on below line we are specifying
                // the style for our text.
                style = MaterialTheme.typography.h6,

                // on below line we are adding
                // padding for our text
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                // on below line we are specifying
                // alignment for our text
                textAlign = TextAlign.Center
            )
        }
    }

    // on below line we are creating a method
    // to get the speech input from user.
    private fun getSpeechInput(context: Context) {
        // on below line we are checking if speech
        // recognizer intent is present or not.
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            // if the intent is not present we are simply displaying a toast message.
            Toast.makeText(context, "Speech not Available", Toast.LENGTH_SHORT).show()
        } else {
            // on below line we are calling a speech recognizer intent
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on the below line we are specifying language model as language web search
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )

            // on below line we are specifying extra language as default english language
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            // on below line we are specifying prompt as Speak something
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something")

            // at last we are calling start activity
            // for result to start our activity.
            startActivityForResult(intent, 101)
        }
    }

    // on below line we are calling our on activity result method to get the output.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // on below line we are checking if the request
        // code is same and result code is ok
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            // if the condition is satisfied we are getting
            // the data from our string array list in our result.
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            // on below line we are setting result
            // in our output text method.
            outputTxt = result?.get(0).toString()
        }
    }

}
