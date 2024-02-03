package com.annuityfarm.annuityfarmapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.databinding.FragmentHomeBinding
import com.annuityfarm.annuityfarmapp.databinding.ProviderDetailsLayoutBinding
import im.delight.android.webview.AdvancedWebView
import org.sufficientlysecure.htmltextview.HtmlResImageGetter


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() , AdvancedWebView.Listener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.providerAdsDetails.setHtml("<ul>" +
                "<li style=\"text-align: left;\">" +
                "<p style=\"color:black;\">Diversified pension investment approach</p>" +
                "</li>" +
                "<li style=\"text-align: left;\">" +
                "<p style=\"color:black;\">Regular review of your financial situation</p>" +
                "</li>" +
                "<li style=\"text-align: left;\">" +
                "<p style=\"color:black;\">Explain your pension plan and align it to your goals</p>" +
                "</li>" +
                "<li style=\"text-align: left;\">" +
                "<p style=\"color:black;\">Team of expert pension advisers</p>" +
                "</li>" +
                "<li style=\"text-align: left;\">" +
                "<p style=\"color:black;\">Knowledge of pension regulations</p>" +
                "</li>" +
                "<li style=\"text-align: left;\">" +
                "<p style=\"color:black;\">Recommend the most appropriate pension solutions</p>" +
                "</li>" +
                "</ul>",
             HtmlResImageGetter(requireContext())
        )

        binding.providerAdsDetails2.setHtml("<h5><span style=\"color:black\">There are good reasons to consider Pension Trustees Liability Insurance from AIG</span></h5>\n" +
                "<p style=\"color:black\">The pension fund trustee liability insurance provides protection for the trustees, the employees, the employer company and the scheme " +
                "itself in event of claims brought against them. The policy provides cover for settlement on legal action and for the costs of defending the action.</p>",
            HtmlResImageGetter(requireContext()))

        binding.providerAdsDetails3.setHtml(
            "<h5><span style=\"color:black\">There are good reasons to consider Pension Trustees Liability Insurance from AIG</span></h5>\n" +
                "<p style=\"color:black\">A Personal Pension is a long-term plan that aims to help you build up a pot of money that you can use for your retirement.</p>" +
            "<h5><span style=\"color:black\">Why invest in a Personal Pension Plan?</span></h5>\n" +
                    "<p style=\"color:black\">Your road to financial freedom begins with our retirement savings plan. If you’re thinking, “But NSSF will be enough” or “But retirement is miles away”, it’s time to rethink.\n" +
                    "\n" +
                    "You can have an adventurous retirement only if you begin planning & saving towards your retirement today.\n" +
                    "\n" +
                    "The excuses end here and the journey to financial freedom begins</p>" +
            "<h5><span style=\"color:black\">Benefits at a glance</span></h5>\n" +
                    "<ul>" +
                    "  <li><p style=\"color:black;\">Tax relive benefits</p></li>" +
                    "  <li><p style=\"color:black;\">Guaranteed Investment rate of 4%</p></li>" +
                    "  <li><p style=\"color:black;\">Guaranteed accumulated capital and Investment income</p></li>" +
                    "  <li><p style=\"color:black;\">No set up fee</p></li>" +
                    "</ul>" ,

            HtmlResImageGetter(requireContext()))

        binding.provider1.setOnClickListener{
            showFullScreenDialog(it)?.show()
        }

        binding.provider1.setOnClickListener{
            showFullScreenDialog(it)?.show()
        }

        binding.provider1.setOnClickListener{
            showFullScreenDialog(it)?.show()
        }
    }


    private fun showFullScreenDialog(view: View): AlertDialog? {
        val displayRectangle = Rect()
        val window: Window = requireActivity().window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val builder = AlertDialog.Builder(requireContext(), R.style.FullScreenAlertDialog)
        val viewGroup: ViewGroup = requireActivity().findViewById(android.R.id.content)
        val dialogBinding: ProviderDetailsLayoutBinding = ProviderDetailsLayoutBinding.inflate(LayoutInflater.from(view.context), viewGroup, false)
        val dialogView:View =  dialogBinding.root

        dialogView.minimumWidth = (displayRectangle.width() * 1f).toInt()
        dialogView.minimumHeight = (displayRectangle.height() * 1f).toInt()
        builder.setView(dialogView)
        val alert = builder.create()

        //Custom control in the dialog
        dialogBinding.imgbCancel.setOnClickListener { alert.dismiss() }
        fullScreenView(dialogBinding, alert)
        return alert
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private var mWebView: AdvancedWebView? = null
    private var dialogBinding:ProviderDetailsLayoutBinding? = null
    private fun fullScreenView(dialogBinding:ProviderDetailsLayoutBinding, alert: AlertDialog){

        this.dialogBinding = dialogBinding
        mWebView = dialogBinding.advancedWebview
        mWebView!!.setListener(requireActivity(), this)
        mWebView!!.setMixedContentAllowed(false)


        dialogBinding.btnViewWikipedia.setOnClickListener {
            val sanlamWebsiteUrl = "https://ibuildglobal.atlassian.net/wiki/spaces/IHD/pages/1678966785/Customer+FAQS#Getting-started%3A"
            when(dialogBinding.btnViewWikipedia.visibility ){

               View.VISIBLE -> {mWebView!!.loadUrl(sanlamWebsiteUrl);dialogBinding.webviewProgressbar.show();dialogBinding.providerDetails.gone();dialogBinding.container.show();dialogBinding.btnViewWikipedia.gone();dialogBinding.btnBack.show();dialogBinding.btnVisitWebsite.let {
                   it.text = resources.getString(R.string.visit_website)
                   it.show()
               }}
               // View.GONE -> {dialogBinding.providerDetails.show();dialogBinding.container.hide();dialogBinding.btnViewWikipedia.show();dialogBinding.btnBack.hide()}
            }
        }

        dialogBinding.btnBack.setOnClickListener {
            when(dialogBinding.btnBack.visibility ){
                //View.GONE  -> {mWebView!!.loadUrl("https://en.wikipedia.org/wiki/sanlam Kenya plc");dialogBinding.providerDetails.hide();dialogBinding.container.show();dialogBinding.btnViewWikipedia.hide();dialogBinding.btnBack.show()}
                View.VISIBLE -> {dialogBinding.providerDetails.show();dialogBinding.container.gone();dialogBinding.btnViewWikipedia.show();dialogBinding.btnBack.gone()}
            }
        }


        dialogBinding.btnVisitWebsite.setOnClickListener {
            when(dialogBinding.btnVisitWebsite.text){
                resources.getString(R.string.visit_website) -> {mWebView!!.loadUrl("https://www.sanlam.com/kenya");dialogBinding.webviewProgressbar.show();dialogBinding.providerDetails.gone();dialogBinding.container.show();dialogBinding.btnVisitWebsite.text = resources.getString(R.string.back); dialogBinding.btnViewWikipedia.let {
                    it.show()
                    dialogBinding.btnBack.gone()
                }}
                resources.getString(R.string.back) -> {dialogBinding.providerDetails.show();dialogBinding.container.gone();dialogBinding.btnVisitWebsite.text = resources.getString(R.string.visit_website);  }
            }
        }

        dialogBinding.btnBackword.setOnClickListener {
            if (!mWebView?.onBackPressed()!!) {
                return@setOnClickListener
            }
        }

    }

    ////////////////////////////////////////////////ADVANCED WEB VIEW//////////////////////////////////////////////////////

    //ADVANCED
     override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).apply {
                binding.toolbarTitle.apply { text = resources?.getString(R.string.annuity_firm) }
                binding.imgTitle.gone()
                binding.imgAnnuityLogo.apply {
                    setBackgroundResource(R.mipmap.annuity_logo)
                    show()
                }
                binding.btnPensionProjectionCalculator.gone()


            }
        }

        mWebView?.onResume()
        // ...

    }

    //@SuppressLint("NewApi")
     override fun onPause() {
        mWebView?.onPause()
        // ...
        super.onPause()
    }

     override fun onDestroy() {
         mWebView?.onDestroy()
        // ...
        super.onDestroy()
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
         mWebView?.onActivityResult(requestCode, resultCode, intent)
        // ...
    }


    override fun onPageStarted(url: String?, favicon: Bitmap?) {}

    override fun onPageFinished(url: String?) {
        dialogBinding?.webviewProgressbar?.gone()
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {}

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {}

}