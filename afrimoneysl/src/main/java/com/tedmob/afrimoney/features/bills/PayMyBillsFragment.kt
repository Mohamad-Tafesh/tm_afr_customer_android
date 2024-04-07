package com.tedmob.afrimoney.features.bills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentPayMyBillsBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("UNREACHABLE_CODE")
@AndroidEntryPoint
class PayMyBillsFragment : BaseVBFragment<FragmentPayMyBillsBinding>() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentPayMyBillsBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            customToolbar.toolbar.run {
                setNavigationIcon(R.drawable.ic_nav_back_white)
                setNavigationOnClickListener { findNavController().popBackStack() }

            }


            img1.load(R.drawable.ic_nawec) {
                transformations(CircleCropTransformation())
            }
            /* img2.load(R.drawable.paymybillsedsa){
                 transformations(CircleCropTransformation())
             }
             img3.load(R.drawable.paymybillsdstv){
                 transformations(CircleCropTransformation())
             }

             img4.load(R.drawable.paymybillsrisingacadamy){
                 transformations(CircleCropTransformation())
             }
             img5.load(R.drawable.paymybillsmercury){
                 transformations(CircleCropTransformation())
             }
             img6.load(R.drawable.paymybillsfcc){
                 transformations(CircleCropTransformation())
             }
             img7.load(R.drawable.paymybillswaec){
                 transformations(CircleCropTransformation())
             }
             img8.load(R.drawable.paymybillspowergen){
                 transformations(CircleCropTransformation())
             }
             img9.load(R.drawable.paymybillscovidtest){
                 transformations(CircleCropTransformation())
             }*/

            nawec.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavNawec()) }

            /*   africellPostPaid.setOnClickListener{findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavPostpaid())}

               dstv.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToChooseMyBillServiceTypeFragment()) }

               risingAcademy.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavRisingAcademy()) }

               mercury.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavMercury()) }

               waec.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavWaec()) }

               fcc.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavFcc()) }

               powergen.setOnClickListener { findNavController().navigate(PayMyBillsFragmentDirections.actionPayMyBillsFragmentToNavPowergen()) }*/
        }


    }

}