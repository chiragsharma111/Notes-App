package com.chrg.notes2

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    var dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle:Bundle?=intent.extras
            id = bundle!!.getInt("ID", 0)
            if(id!=0) {
                etAddNotesTitle.setText(bundle!!.getString("Name").toString())
                etAddNotesDes.setText(bundle!!.getString("Des").toString())
            }
        }catch (ex:Exception){}


    }

    fun buAdd(view: View) {
//        finish() // to close activity

        var dbManager = dbDatabase(this)
        var values= ContentValues()
        values.put("Title", etAddNotesTitle.text.toString())
        values.put("Description", etAddNotesDes.text.toString())

        if(id==0){ // New Note
            var ID = dbManager.Insert(values)
            if(ID>0) {
                Toast.makeText(this, "Note is Added", Toast.LENGTH_LONG).show()
                finish()
            }
            else { // Existing Note
                Toast.makeText(this, "Note can not be Added", Toast.LENGTH_LONG).show()
            }
        }
        else {
            var selectionArgs = arrayOf(id.toString())
            var ID = dbManager.Update(values, "ID=?", selectionArgs)
            if(ID>0) {
                Toast.makeText(this, "Note is Added", Toast.LENGTH_LONG).show()
                finish()
            }
            else { // Existing Note
                Toast.makeText(this, "Note can not be Added", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}
