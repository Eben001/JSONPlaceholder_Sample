package com.ebenezer.gana.jsonplaceholdersample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebenezer.gana.jsonplaceholdersample.databinding.FragmentPostsBinding
import com.ebenezer.gana.jsonplaceholdersample.ui.adapters.PostsListAdapter
import com.ebenezer.gana.jsonplaceholdersample.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "PostsFragment"

@AndroidEntryPoint
class PostsFragment : Fragment() {
    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostsViewModel by viewModels()
    private lateinit var postsListAdapter: PostsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        setupAdapter()
        setupRecyclerView()
        subscribeToObservables()

    }

    private fun setOnClickListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            startRefreshing()
            viewModel.getPosts()
        }
    }

    private fun setupAdapter() {
        postsListAdapter = PostsListAdapter {

        }
    }

    private fun setupRecyclerView() {
        binding.rvPosts.apply {
            adapter = postsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun subscribeToObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.postsResult.collectLatest { response ->
                        when (response) {
                            is Result.Loading -> {
                                startRefreshing()
                            }
                            is Result.Success -> {
                                stopRefreshing()
                                response.data.let { posts ->
                                    postsListAdapter.submitList(posts)
                                }
                            }
                            else -> {
                                stopRefreshing()
                            }
                        }
                    }
                }

                launch {
                    viewModel.errorMessageSharedFlow.collectLatest { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        stopRefreshing()
                    }
                }
            }
        }

    }

    private fun startRefreshing() {
        binding.swipeRefresh.isRefreshing = true
    }

    private fun stopRefreshing() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}