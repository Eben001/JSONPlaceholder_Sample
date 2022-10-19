package com.ebenezer.gana.jsonplaceholdersample.ui.postDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import com.ebenezer.gana.jsonplaceholdersample.databinding.FragmentPostDetailsBinding
import com.ebenezer.gana.jsonplaceholdersample.ui.adapters.CommentsListAdapter
import com.ebenezer.gana.jsonplaceholdersample.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostDetailsViewModel by viewModels()
    private val args: PostDetailsFragmentArgs by navArgs()
    private lateinit var commentListAdapter: CommentsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val post = args.postItem
        bindViews(post)
        viewModel.getComments(post.id)
        setOnClickListeners(post.id)
        setupCommentAdapter()
        setupCommentRecyclerView()
        subscribeToObservables()

    }

    private fun bindViews(postItem: PostItem) {
        binding.apply {
            title.text = postItem.title
            postBody.text = postItem.body
        }
    }

    private fun setOnClickListeners(postId: Int) {
        binding.swipeRefreshComment.setOnRefreshListener {
            startRefreshing()
            viewModel.getComments(postId)
        }
    }

    private fun setupCommentAdapter() {
        commentListAdapter = CommentsListAdapter()
    }

    private fun setupCommentRecyclerView() {
        binding.rvComments.apply {
            adapter = commentListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun subscribeToObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.commentResult.collectLatest { response ->
                        when (response) {
                            is Result.Loading -> {
                                startRefreshing()
                            }
                            is Result.Success -> {
                                stopRefreshing()
                                response.data.let { posts ->
                                    commentListAdapter.submitList(posts)
                                }
                                if (binding.rvComments.visibility != View.VISIBLE) {
                                    binding.rvComments.visibility = View.VISIBLE
                                    binding.errorMessage.visibility = View.GONE
                                }
                            }
                            else -> {
                                stopRefreshing()
                            }
                        }
                    }
                }

                launch {
                    viewModel.errorMessageSharedFlow.collectLatest {
                        stopRefreshing()
                        binding.errorMessage.text = it
                        binding.errorMessage.visibility = View.VISIBLE
                        binding.rvComments.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun startRefreshing() {
        binding.swipeRefreshComment.isRefreshing = true
    }

    private fun stopRefreshing() {
        if (binding.swipeRefreshComment.isRefreshing) {
            binding.swipeRefreshComment.isRefreshing = false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}