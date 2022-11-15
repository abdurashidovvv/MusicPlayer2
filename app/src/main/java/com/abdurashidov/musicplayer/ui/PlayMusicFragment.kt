package com.abdurashidov.musicplayer.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.abdurashidov.musicplayer.R
import com.abdurashidov.musicplayer.databinding.FragmentPlayMusicBinding
import com.abdurashidov.musicplayer.models.AudioVisualizer
import com.abdurashidov.musicplayer.models.Music
import com.abdurashidov.musicplayer.models.MyData
import java.util.ArrayList
import kotlin.time.Duration.Companion.milliseconds

class PlayMusicFragment : Fragment() {

    private lateinit var binding: FragmentPlayMusicBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private lateinit var musicList: ArrayList<Music>
    private lateinit var audioVisualizer: AudioVisualizer
    private var index = -2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicList = ArrayList()
        musicList.addAll(MyData.musicList)
        mediaPlayer = MyData.path!!
        index = MyData.index

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayMusicBinding.inflate(layoutInflater)



        binding.songAuthor.text = musicList[index].author
        // play/pause buttons
        binding.playPause.setOnClickListener {
            if (MyData.path!!.isPlaying) {
                binding.playPauseImage.setImageResource(R.drawable.play_button)
                MyData.path!!.pause()
            } else {
                binding.playPauseImage.setImageResource(R.drawable.pause)
                MyData.path!!.start()
            }
        }

        //Last button click
        binding.back.setOnClickListener {
            mediaPlayer.stop()
            index = --index
            if (index <= -1) index = RecycleMusicFragment.musicList.size - 1
            mediaPlayer = MediaPlayer.create(
                binding.root.context,
                Uri.parse(RecycleMusicFragment.musicList[index].musicPath)
            )
            MyData.path = mediaPlayer
            binding.songName.text = RecycleMusicFragment.musicList[index].title
            binding.songAuthor.text = RecycleMusicFragment.musicList[index].author
            val duration = MyData.path!!.duration.milliseconds
            binding.musicDuration.text =
                "${duration.inWholeMinutes}:${duration.inWholeSeconds - duration.inWholeMinutes * 60}"
            binding.playPauseImage.setImageResource(R.drawable.pause)
            mediaPlayer.start()
        }

        //Next button click
        binding.next.setOnClickListener {
            mediaPlayer.stop()
            index = ++index
            if (index >= RecycleMusicFragment.musicList.size) index = 0
            mediaPlayer = MediaPlayer.create(
                binding.root.context,
                Uri.parse(RecycleMusicFragment.musicList[index].musicPath)
            )
            MyData.path = mediaPlayer
            binding.songName.text = RecycleMusicFragment.musicList[index].title
            binding.songAuthor.text = RecycleMusicFragment.musicList[index].author
            val duration = MyData.path!!.duration.milliseconds
            binding.musicDuration.text =
                "${duration.inWholeMinutes}:${duration.inWholeSeconds - duration.inWholeMinutes * 60}"
            binding.playPauseImage.setImageResource(R.drawable.pause)
            mediaPlayer.start()
        }

        //Duration
        val duration = MyData.path!!.duration.milliseconds
        binding.musicDuration.text =
            "${duration.inWholeMinutes}:${duration.inWholeSeconds - duration.inWholeMinutes * 60}"

        //seekBar set Music
        binding.seekBarDistance.max = MyData.path?.duration!!
        binding.seekBarDistance.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MyData.path!!.seekTo(progress)
                }


                val currentTime = MyData.path!!.currentPosition
                if (currentTime.toString().length > 3) {
                    val second =
                        currentTime.toString().subSequence(0, currentTime.toString().length - 3)
                    val minut = second.toString().toInt() / 60
                    val currentSecond = second.toString().toInt() - minut * 60

                    if (minut.toString().length == 0) {
                        if (currentSecond.toString().length == 1) {
                            binding.musicCurrentTime.text = "00:0$currentSecond"
                        } else if (currentSecond.toString().length == 2) {
                            binding.musicCurrentTime.text = "00:$currentSecond"
                        }
                    } else if (minut.toString().length == 2) {
                        if (currentSecond.toString().length == 1) {
                            binding.musicCurrentTime.text = "00:0$currentSecond"
                        } else if (currentSecond.toString().length == 2) {
                            binding.musicCurrentTime.text = "00:$currentSecond"
                        }
                    } else if (minut.toString().length == 1) {
                        if (currentSecond.toString().length == 1) {
                            binding.musicCurrentTime.text = "0$minut:0$currentSecond"
                        } else if (currentSecond.toString().length == 2) {
                            binding.musicCurrentTime.text = "0$minut:$currentSecond"
                        }
                    } else if (minut.toString().length == 2) {
                        if (currentSecond.toString().length == 1) {
                            binding.musicCurrentTime.text = "$minut:0$currentSecond"
                        } else if (currentSecond.toString().length == 2) {
                            binding.musicCurrentTime.text = "$minut:$currentSecond"
                        }
                    }
                }

                previousMusic()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        seekBarChanged()

        //back Fragment
        binding.backFragment.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    fun seekBarChanged(){
        handler= Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 300)
    }

    val runnable=object : Runnable{
        override fun run() {
            binding.seekBarDistance.progress=MyData.path!!.currentPosition
            handler.postDelayed(this, 300)
        }
    }




    private fun previousMusic(){
        if (mediaPlayer!=null){
            mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener{
                @SuppressLint("SetTextI18n")
                override fun onCompletion(mp: MediaPlayer?) {
                    mediaPlayer.stop()
                    ++index
                    if (index >= musicList.size) index = 0
                    mediaPlayer=MediaPlayer.create(binding.root.context, Uri.parse(musicList[index].musicPath))
                    mediaPlayer.start()
                    MyData.path=mediaPlayer
                    binding.songName.text=musicList[index].title
                    binding.songAuthor.text=musicList[index].author
                    val duration = MyData.path!!.duration.milliseconds
                    binding.musicDuration.text =
                        "${duration.inWholeMinutes}:${duration.inWholeSeconds - duration.inWholeMinutes * 60}"
                    MyData.index=index
                    MyData.path=mediaPlayer
                }
            })
        }
    }
}
