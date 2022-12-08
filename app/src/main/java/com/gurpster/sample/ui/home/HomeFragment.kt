package com.gurpster.sample.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import com.gurpster.octopus.BindingFragment
import com.gurpster.octopus.DeviceInfo
import com.gurpster.sample.databinding.FragmentHomeBinding
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.KeyProps
import com.yakivmospan.scytale.Store
import java.math.BigInteger
import java.util.*
import javax.security.auth.x500.X500Principal


class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deviceInfo = DeviceInfo()
        Log.d("deviceInfo", deviceInfo.toString())
        val deviceId = deviceInfo.generateUUID()
        Log.d("deviceId", deviceId)

        // Create store with specific name and password
        // Create store with specific name and password
        val store = Store(requireContext(), "STORE_NAME", "STORE_PASSWORD".toCharArray())

        val alias = "alias"
        val password = "password".toCharArray()
        val keysize = 512

        val start: Calendar = Calendar.getInstance()
        val end: Calendar = Calendar.getInstance()
        end.add(Calendar.YEAR, 1)

        val keyProps = KeyProps.Builder()
            .setAlias(alias)
            .setPassword(password)
            .setKeySize(keysize)
            .setKeyType("RSA")
            .setSerialNumber(BigInteger.ONE)
            .setSubject(X500Principal("CN=$alias CA Certificate"))
            .setStartDate(start.time)
            .setEndDate(end.time)
            .setBlockModes("ECB")
            .setEncryptionPaddings("PKCS1Padding")
            .setSignatureAlgorithm("SHA256WithRSAEncryption")
            .build()

        // Generate KeyPair depending on KeyProps
        // Generate KeyPair depending on KeyProps
        val keyPair = store.generateAsymmetricKey(keyProps)

        val k = keyPair.private
        Log.d("private", "$k")
        val p = keyPair.public
        Log.d("public", "$p")

        val encryptionBlockSize = keysize / 8 - 11 // as specified for RSA/ECB/PKCS1Padding keys

        val decryptionBlockSize = keysize / 8 // as specified for RSA/ECB/PKCS1Padding keys

        val crypto = Crypto("RSA/ECB/PKCS1Padding", encryptionBlockSize, decryptionBlockSize)

//        val text = "Sample text"
//        val encryptedData = crypto.encrypt(text, key, false)
//        val decryptedData = crypto.decrypt(encryptedData, key, false)

        Log.d("deviceId", "$keyProps")

    }

}