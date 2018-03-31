package bstu.by.lab6k

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bstu.by.lab6k.database.DBHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: SQLiteDatabase
    var values = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helper = DBHelper(this)
        database = helper.writableDatabase

        btnInsert.setOnClickListener {

            val text = etData.text.toString()
            DBHelper.addValue(text, database)
            values.add(text)

            (lvData.adapter as ValueAdapter).notifyDataSetChanged()

            etData.text.clear()
            Toast.makeText(this, "Данные добавлены", Toast.LENGTH_SHORT).show()
        }

        btnReadDB.setOnClickListener {
            values = DBHelper.getAllValues(database)

            val valuesAdapter = ValueAdapter(this, values)

            lvData.adapter = valuesAdapter
        }

        btnUpdate.setOnClickListener {
            val old = etUpdateOld.text.toString()
            val new = etUpdateNew.text.toString()

            values[values.indexOf(old)] = new

            DBHelper.updateValue(old, new, database)
            (lvData.adapter as ValueAdapter).notifyDataSetChanged()

            etUpdateNew.text.clear()
            etUpdateOld.text.clear()
            Toast.makeText(this, "Данные обновлены", Toast.LENGTH_SHORT).show()
        }

        btnDelete.setOnClickListener {
            val delete = etDelete.text.toString()

            DBHelper.deleteValue(delete, database)

            values.remove(delete)
            (lvData.adapter as ValueAdapter).notifyDataSetChanged()

            etDelete.text.clear()
            Toast.makeText(this, "Данные удалены", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ValueAdapter: BaseAdapter {

        private var valuesList = ArrayList<String>()
        private var context: Context? = null

        constructor(context: Context, valuesList: ArrayList<String>) : super() {
            this.valuesList = valuesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.item, parent, false )
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.value.text = valuesList[position]

            return view
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return valuesList.size
        }

        override fun getItem(p0: Int): Any {
            return values[p0]
        }
    }

    private class ViewHolder(view: View?) {
        val value: TextView = view?.findViewById<TextView>(R.id.tvData) as TextView
    }
}
