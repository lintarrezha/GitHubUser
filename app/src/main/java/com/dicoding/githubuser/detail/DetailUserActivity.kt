package com.dicoding.githubuser.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.response.DetailUserResponse
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object{
        const val ITEM_ID = "item_id"
        private val TAB_TITLE = intArrayOf(
            R.id.tvFollowing,
            R.id.tvFollowers
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = intent.getStringExtra(ITEM_ID)

        detailViewModel.setDetailUser(detailUserId = items.toString())
        supportActionBar?.hide()

        detailViewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager2)
        viewPager.adapter = sectionsPagerAdapter

        val tabLayoutMediator = TabLayoutMediator(binding.tabs, binding.viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
            when (position) {
                0 -> {
                    tab.text = getString(R.string.tab_Following)
                }
                1 -> {
                    tab.text = getString(R.string.tab_Followers)
                }
                else -> {

                }
            }
        }
        tabLayoutMediator.attach()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val actionBar = getSupportActionBar()

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun setDetailUser(detailUserResponse: DetailUserResponse) {
        binding.tvNamaProfile.text = detailUserResponse.login
        binding.tvUsernameProfile.text = detailUserResponse.name
        binding.tvFollowing.text = resources.getString(R.string.following, detailUserResponse.following)
        binding.tvFollowers.text = resources.getString(R.string.follower, detailUserResponse.followers) //hardcoded
        Glide.with(binding.root.context)
            .load(detailUserResponse.avatarUrl)
            .circleCrop()
            .into(binding.ivProfile)
    }

    private fun showLoading(state: Boolean) { binding.progressBarDetail.visibility = if (state) View.VISIBLE else View.GONE }
}