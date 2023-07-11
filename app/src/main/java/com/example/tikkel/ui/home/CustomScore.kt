package com.example.tikkel.ui.home

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tikkel.databinding.CustomScoreBinding


class CustomScore : DialogFragment(){
    private var _binding:CustomScoreBinding? = null
    private val binding get() = _binding!!
    // 추후 데이터 베이스 파트
    var datelist = mutableListOf<String>("2022-01-02","2022-01-03")
    var valuelist = mutableListOf<String>("4p","6p")
    var contentlist = mutableListOf<String>("퀘스트 성공","퀘스트 성공")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CustomScoreBinding.inflate(inflater, container, false)
        var view = binding.root
        val UseScore = mutableListOf<UseScore>()
        val StoreScore = mutableListOf<StoreScore>()

        for(i in valuelist.indices){
            UseScore.add(UseScore(datelist[i],valuelist[i],contentlist[i]))
            StoreScore.add(StoreScore(datelist[i],valuelist[i],contentlist[i]))
        }

        val storeListAdapter = StoreListAdapter(StoreScore)
        val useListAdapter = UseListAdapter(UseScore)
        // 추가하기 UseScore.add(UseScore("","",""))

        binding.pointStoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storeListAdapter
        }

        binding.pointUseRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = useListAdapter
        }

        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500

        binding.pointDetailBtn01.setOnClickListener {
            binding.pointDetailBtn01.isSelected=binding.pointDetailBtn01.isSelected!=true
            if(binding.pointDetailBtn01.isSelected) {
                binding.pointStoreRecyclerView.layoutParams.height = 600
                binding.pointDetailBtn01.text="접기▲"
                binding.pointStoreRecyclerView.startAnimation(fadeInAnimation)
                binding.pointStoreRecyclerView.visibility = View.VISIBLE
            }
            else{
                binding.pointDetailBtn01.text="자세히보기▼"
                binding.pointStoreRecyclerView.startAnimation(fadeInAnimation)
                binding.pointStoreRecyclerView.visibility = View.GONE
            }
        }

        binding.pointDetailBtn02.setOnClickListener {
            binding.pointDetailBtn02.isSelected = binding.pointDetailBtn02.isSelected != true
            if (binding.pointDetailBtn02.isSelected) {
                binding.pointUseRecyclerView.layoutParams.height = 600
                binding.pointDetailBtn02.text = "접기▲"
                binding.pointUseRecyclerView.startAnimation(fadeInAnimation)
                binding.pointUseRecyclerView.visibility = View.VISIBLE
            } else {
                binding.pointDetailBtn02.text = "자세히보기▼"
                binding.pointUseRecyclerView.startAnimation(fadeInAnimation)
                binding.pointUseRecyclerView.visibility = View.GONE
            }
        }
        // 화면 꽉차게 설정
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val deviceSize = Point()
        display.getSize(deviceSize)

        binding.dialogMain.layoutParams.width = deviceSize.x
        binding.dialogMain.layoutParams.height= deviceSize.y

        val window = dialog!!.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog?.show()
        binding.backBtnToMyPage.setOnClickListener {
            dismiss()    // 대화상자를 닫는 함수
        }

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}