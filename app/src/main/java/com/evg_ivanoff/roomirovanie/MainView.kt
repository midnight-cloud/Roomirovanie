package com.evg_ivanoff.roomirovanie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evg_ivanoff.roomirovanie.data.UserDataBase
import com.evg_ivanoff.roomirovanie.databinding.ActivityMainViewBinding
import com.evg_ivanoff.roomirovanie.domain.UserAdapter
import com.evg_ivanoff.roomirovanie.domain.UserRoomDataBase
import com.evg_ivanoff.roomirovanie.presenter.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainView : AppCompatActivity()  {
    private val binding by lazy {
        ActivityMainViewBinding.inflate(layoutInflater)
    }
    private val db by lazy {
        UserRoomDataBase.getDatabase(this)
    }
    private val userAdapter = UserAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            rvMain.apply {
                layoutManager = LinearLayoutManager(this@MainView)
                adapter = userAdapter
            }
        }

        db.userDao().getAllUsers().asLiveData().observe(this){
            userAdapter.submitList(it)
        }

        userAdapter.onItemClickListener = {
            startEditMode(it)
        }

        userAdapter.onItemLongClickListener = {

        }

        setupSwipeListener(binding.rvMain)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MODE, MODE_ADD)
            startActivity(intent)
        }
    }

    private fun startEditMode(user: UserDataBase) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MODE, MODE_EDIT)
        intent.putExtra("User", user)
        startActivity(intent)
    }

    private fun setupSwipeListener(rvDb: RecyclerView) {
        //удаление по свайпу влево или вправо
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = userAdapter.currentList[viewHolder.adapterPosition]
                CoroutineScope(Dispatchers.Main).launch {
                    db.userDao().deleteUserById(item.id!!)
                }
                Toast.makeText(this@MainView, "${item.name} deleted", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvDb)
    }



    companion object {
        val MODE = "MODE"
        val MODE_ADD = "MODE_ADD"
        val MODE_EDIT = "MODE_EDIT"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> {
                CoroutineScope(Dispatchers.Main).launch {
                    db.userDao().deleteAllUsers()
                }
                Toast.makeText(this, "All users has been deleted", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}