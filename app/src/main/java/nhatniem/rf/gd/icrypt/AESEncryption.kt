package nhatniem.rf.gd.icrypt

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryption {
    companion object {
        const val ALGO_IMAGE_ENCRYPTOR = "AES/OFB"
        const val ALGO_SECRET_KEY = "AES"
        const val SPEC = "EobXYfeVLaE4lvu2"
      //  const val KEY_STR = "16byte"
        const val MEGABYTE_SIZE = 1024 * 1024 * 7

        const val TAG = "AESEncryption"

        //Image Name
        const val TEMP_IMAGE_TAG = "temp_"
    }

    fun encrypt(content: String?, secureKey: String): ByteArray? {
        if (content == null) {
            return null
        }
        try {
            // Get key data
            val rawKeyData: ByteArray =
                getAESKey(
                    secureKey
                )
            // Create a KeySpec object from the original key data
            val key =
                SecretKeySpec(rawKeyData, ALGO_SECRET_KEY)
            // Cipher object actually completes the encryption operation
            val cipher = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            // Initialize the Cipher object with a key
            val initParam: ByteArray =
                SPEC.toByteArray()
            val ivParameterSpec =
                IvParameterSpec(initParam)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec)
            //mỗi Khối mã được mã hóa ở chế độ OFB, vector khởi tạo IV và khóa Bí mật được đưa vào Hàm mã hóa Ek, kết quả đầu ra được XOR với bản rõ tạo nên Khối Mã
            return cipher.doFinal(content.toByteArray())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        return null
    }
    fun decrypt(content: ByteArray?,  secureKey: String): String {
        if (content == null) {
            return ""
        }
        try {
            // Get key data
            val rawKeyData: ByteArray =
                getAESKey(
                    secureKey
                ) // secureKey.getBytes();
            // Create a KeySpec object from the original key data
            val key = SecretKeySpec(rawKeyData, ALGO_SECRET_KEY)
            // Cipher object actually completes the decryption operation
            val cipher = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            // Initialize the Cipher object with a key
            val initParam: ByteArray =
                SPEC.toByteArray()
            val ivParameterSpec = IvParameterSpec(/* iv = */ initParam)
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
            return String(cipher.doFinal(content), Charset.defaultCharset())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        return ""
    }
    @Throws(UnsupportedEncodingException::class)
    fun getAESKey(key: String): ByteArray {
        val keyBytes: ByteArray
        keyBytes = key.toByteArray(charset("UTF-8"))
        val keyBytes16 =
            ByteArray(key.length)
        System.arraycopy(
            keyBytes,
            0,
            keyBytes16,
            0,
            Math.min(
                keyBytes.size,
                key.length
            )
        )
        return keyBytes16
    }
    fun encryptFile(originalFilePath: String,encryptedImagePath: String, KEY_STR: String, SPEC: String): String {
       // val encryptedImagePath = createCopyOfOriginalFile(originalFilePath)
        Log.e("originalFilePath", originalFilePath)
        try {
            val inputStream = FileInputStream(originalFilePath)
            val iv = IvParameterSpec(SPEC.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(KEY_STR.toByteArray(Charsets.UTF_8), ALGO_SECRET_KEY)
            val cipher = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
           // println(encryptedImagePath)
            val outputStream = FileOutputStream(File(encryptedImagePath))
            CipherOutputStream(outputStream, cipher).use {
                it.write(inputStream.readBytes())
                it.flush()
                it.close()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }
        catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        //Delete original file
     //  FileUtils.deleteFile(originalFilePath)

        //Rename encrypted image file to original name
      //  val createdFile = renameImageToOriginalFileName(encryptedImagePath)
       // Log.e("createdFile", createdFile)
      //  (File(createdFile))
        //FileUtils.deleteFile(originalFilePath)
        return encryptedImagePath

    }

    fun decryptFile(originalFilePath: String, decryptedFilePath:String,KEY_STR: String, SPEC: String): String {
       // val decryptedFilePath = createCopyOfOriginalFile(originalFilePath)
        try {
            val fis = FileInputStream(originalFilePath)
            val iv = IvParameterSpec(SPEC.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(KEY_STR.toByteArray(Charsets.UTF_8), ALGO_SECRET_KEY)
            val cipher = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)

            val out = CipherInputStream(fis, cipher)

            File(decryptedFilePath).outputStream().use {
                out.copyTo(it)
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e("errorNoSuchAlgorithm", "${e.message}")
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
            Log.e("errorBlockSizeException", "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return decryptedFilePath

    }


    private fun renameImageToOriginalFileName(path: String): String {
        val filePath = AESEncryption().getImageParentPath(path)
        val imageName = AESEncryption().getImageNameFromPath(path)

        val from = File(filePath, imageName)

        val renameTo = imageName!!.replace(TEMP_IMAGE_TAG, "")

        val to = File(filePath, renameTo)
        if (from.exists())
            from.renameTo(to)

        return to.path

    }

    private fun createCopyOfOriginalFile(originalFilePath: String): String {

        val filePath = AESEncryption().getImageParentPath(originalFilePath)
        val imageName = AESEncryption().getImageNameFromPath(originalFilePath)

        val originalFile = File(originalFilePath)
        val copyFile = File(filePath, "$TEMP_IMAGE_TAG$imageName")

        //Create a copy of original file
        try {
            FileUtils.copy(originalFile, copyFile)

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return copyFile.path

    }

    private fun getImageParentPath(path: String?): String? {
        var newPath = ""
        path?.let {
            newPath = it.substring(0, it.lastIndexOf("/") + 1)
        }
        return newPath
    }

    private fun getImageNameFromPath(path: String?): String? {
        var newPath = ""
        path?.let {
            newPath = it.substring(it.lastIndexOf("/") + 1)
        }
        return newPath
    }

}