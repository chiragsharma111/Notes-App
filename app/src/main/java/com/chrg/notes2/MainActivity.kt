package com.chrg.notes2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show()
        // Add dummy Values
        // Load from Database

        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show()
    }
    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show()
    }
    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show()
    }
    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "onStop", Toast.LENGTH_LONG).show()
    }
    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show()
    }
    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show()
    }

    fun LoadQuery(title:String) {

        var dbManager = dbDatabase(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()) {

            do{

                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Des = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Des))

            }while (cursor.moveToNext())
        }
        var myNotesAdapter = NotesAdapter(this, listNotes)
        lvNotes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                LoadQuery("%$query%")
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
//                TODO("Not yet implemented")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when(item.itemId) {
                R.id.addNote->{
                    // Go to Add Notes page
                    var intent = Intent(this, AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class NotesAdapter: BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context, listNotesAdapter: ArrayList<Note>) :super() {
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myNote = listNotesAdapter[position]
            myView.tvTitle.text = myNote.noteName
            myView.tvDes.text = myNote.noteDes
            myView.ivDelete.setOnClickListener( View.OnClickListener {
                var dbManager = dbDatabase(this.context!!)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.Delete("ID=?", selectionArgs)
                LoadQuery("%")
            })
            myView.ivEdit.setOnClickListener( View.OnClickListener {
                GoToUpdate(myNote)
            })
            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }

    fun GoToUpdate(note: Note) {

        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("ID", note.noteID)
        intent.putExtra("Name", note.noteName)
        intent.putExtra("Des", note.noteDes)
        startActivity(intent)
    }

}
