package com.dicoding.storyapp1.view.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp1.data.Stories
import com.dicoding.storyapp1.data.UserRepository
import com.dicoding.storyapp1.myutil.DataDummy
import com.dicoding.storyapp1.myutil.MainDispatcherRule
import com.dicoding.storyapp1.myutil.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var userRepository: UserRepository

    private val dummyStoriesResponse = DataDummy.generateDummyResponse()
    private val nullDummyStoriesResponse: MutableList<Stories> = arrayListOf()
    private var mockedStatic: MockedStatic<Log>? = null

    @SuppressLint("CheckResult")
    @Before
    fun init() {
        mockedStatic = Mockito.mockStatic(Log::class.java)
    }

    @After
    fun close() {
        mockedStatic?.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when getStories Should Not Null and Return Success`() = runTest {
        val data: PagingData<Stories> =
            StoryPagingSource.snapshot(dummyStoriesResponse)
        val expectedStories = MutableLiveData<PagingData<Stories>>()
        expectedStories.value = data
        Mockito.`when`(userRepository.getStories("token")).thenReturn(expectedStories)

        val viewModel = MainViewModel(userRepository)
        val actualStories: PagingData<Stories> = viewModel.getStories("token").getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = RvStoriesAdapter.diffUtil,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStoriesResponse.size, differ.snapshot().size)
        assertEquals(dummyStoriesResponse, differ.snapshot())
        assertEquals(dummyStoriesResponse[0], differ.snapshot()[0])
    }

    @Test
    fun `when getStories Should Empty and Return Null`() = runTest {
        val data: PagingData<Stories> =
            StoryPagingSource.snapshot(nullDummyStoriesResponse)
        val expectedStories = MutableLiveData<PagingData<Stories>>()
        expectedStories.value = data
        Mockito.`when`(userRepository.getStories("token")).thenReturn(expectedStories)

        val viewModel = MainViewModel(userRepository)
        val actualStories: PagingData<Stories> = viewModel.getStories("token").getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = RvStoriesAdapter.diffUtil,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        assertEquals(nullDummyStoriesResponse.size, differ.snapshot().size)
    }

    @OptIn(ExperimentalPagingApi::class)
    class StoryPagingSource : PagingSource<Int, Stories>() {
        companion object {
            fun snapshot(items: List<Stories>): PagingData<Stories> {
                return PagingData.from(items)
            }

        }

        override fun getRefreshKey(state: PagingState<Int, Stories>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stories> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}