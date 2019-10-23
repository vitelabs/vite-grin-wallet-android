package net.vite.wallet.grin

import java.io.File

/**
 * init, then call the function named end with 'KT'
 *
 * Docs at https://github.com/vitelabs/Vite_GrinWallet/tree/8b08aa50fdb8bf5152747b0ce4271fa352822c0c
 */
class GrinBridge {

    fun handleCResult(result: String): ResultType {
        val resultType = ResultType()
        resultType.code = result.substring(0, 1).toInt()
        resultType.result = result.substring(1)
        return resultType
    }

    lateinit var chainType: String
    lateinit var walletPath: String
    lateinit var password: String
    lateinit var checkNodeApiHttpAddr: String
    lateinit var apiSecret: String
    var account = "default"

    fun init(
            chainType: String,
            walletPath: String,
            password: String,
            checkNodeApiHttpAddr: String,
            apiSecret: String
    ) {
        this.walletPath = walletPath
        this.chainType = chainType
        this.password = password
        this.apiSecret = apiSecret
        this.checkNodeApiHttpAddr = checkNodeApiHttpAddr
        checkDirectories()
        checkApiSecret()
    }

    fun walletExists(): Boolean {
        return File("$walletPath/wallet_data/wallet.seed").exists()
    }

    private external fun balance(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean
    ): String

    fun walletInfo(refreshFromNode: Boolean, callback: (result: ResultType) -> Unit): String {
        Thread {
            val balance = balance(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    refreshFromNode
            )
            callback.invoke(handleCResult(balance))
        }.start()

        return ""
    }

    private external fun txsGet(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean
    ): String

    fun txsGetKT(refreshFromNode: Boolean): String {
        return txsGet(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                refreshFromNode
        )
    }

    private external fun txGet(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean,
            tx_id: Int
    ): String


    fun txGetKT(refreshFromNode: Boolean, txId: Int): String {
        return txGet(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                refreshFromNode, txId
        )
    }

    private external fun txCreate(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            message: String,
            amount: Long,
            selection_strategy_is_use_all: Boolean
    ): String

    private external fun txStrategies(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            amount: Long
    ): String

    fun txStrategiesKT(amount: Long): String {
        return txStrategies(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                amount
        )
    }

    private external fun txCancel(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            id: Int
    ): String

    fun txCancelKT(id: Int): String {
        return txCancel(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                id
        )
    }

    private external fun txReceive(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            slate_path: String,
            message: String
    ): String

    fun txReceiveKT(
            slatePath: String,
            message: String,
            callback: (result: String) -> Unit
    ): String {
        val txReceive = txReceive(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                slatePath, message
        )
        callback.invoke(txReceive)
        return txReceive
    }

    private external fun txFinalize(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            slate_path: String
    ): String

    fun txFinalizeKT(slatePath: String): String {
        return txFinalize(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                slatePath
        )
    }

    private external fun txSend(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            amount: Long,
            selection_strategy_is_use_all: Boolean,
            message: String,
            dest: String
    ): String

    fun txSendKT(
            amount: Long,
            selectionStrategyIsUseAll: Boolean,
            message: String,
            dest: String
    ): String {
        return txSend(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                amount,
                selectionStrategyIsUseAll,
                message,
                dest
        )
    }

    private external fun txRepost(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            tx_slate_id: String
    ): String

    fun txRepostKT(txSlateId: String): String {
        return txRepost(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                txSlateId
        )
    }

    private external fun walletInit(
            j_recipient: String,
            j_chain_type: String,
            j_password: String,
            j_check_node_api_http_addr: String
    ): String

    fun walletInitKT(): String {
        return walletInit(
                walletPath,
                chainType,
                password,
                checkNodeApiHttpAddr
        )
    }

    private external fun walletPhrase(
            j_path: String,
            j_chain_type: String,
            j_password: String,
            j_check_node_api_http_addr: String
    ): String

    fun walletPhraseKT(): String {
        return walletPhrase(
                walletPath,
                chainType,
                password,
                checkNodeApiHttpAddr
        )
    }

    private external fun walletRecovery(
            path: String,
            chain_type: String,
            phrase: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    private external fun walletCheck(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    private external fun walletRestore(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    fun walletRestoreKT(): String {
        Thread {
            var result =
                    walletRestore(walletPath, chainType, account, password, checkNodeApiHttpAddr)
            println("xirtam grin_wallet_restore_kt result: $result")
        }.start()
        return ""
    }

    private external fun height(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    fun heightKT(): String {
        return height(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr
        )
    }

    private external fun outputsGet(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean
    ): String

    fun outputsGetKT(refreshFromNode: Boolean): String {
        return outputsGet(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                refreshFromNode
        )
    }


    private external fun outputGet(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean,
            tx_id: Int
    ): String

    fun outputGetKT(refreshFromNode: Boolean, txId: Int): String {
        return outputGet(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                refreshFromNode,
                txId
        )
    }


    fun isResponseSlate(slatePath: String): String {
//        return slatePath.components(separatedBy: ".").last == "response" || slatePath.contains("response")
        return ""
    }

    fun getSlateUrl(slateId: String, isResponse: Boolean): String {
//        let path = "\(walletUrl.path)/slates/\(slateId).grinslate\(isResponse ? ".response" : "")"
//        return URL(fileURLWithPath: path)
        return ""
    }

    private fun checkDirectories() {
        val file = File("$walletPath")
        if (!file.exists()) file.mkdirs()
        val file1 = File("$walletPath/wallet_data")
        if (!file1.exists()) file1.mkdirs()
        val file2 = File("$walletPath/slates")
        if (!file2.exists()) file2.mkdirs()
    }

    private fun checkApiSecret() {
        val file = File("$walletPath/.api_secret")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(apiSecret)
        }
    }

    fun walletRecoveryKT(phrase: String): String {
        Thread {
            var result =
                    walletRecovery(walletPath, chainType, phrase, password, checkNodeApiHttpAddr)
            println("xirtam grin_wallet_recovery_kt result:  $result")
        }.start()
        return ""
    }

    fun walletCheckKT(): String {
        Thread {
            var result = walletCheck(walletPath, chainType, account, password, checkNodeApiHttpAddr)
            println("xirtam grin_wallet_check_kt result: $result")
        }.start()
        return ""
    }

    external fun hello(
            j_recipient: String
    ): String

    fun txCreateKT(message: String, amount: Long, selectionStrategyIsUseAll: Boolean): String {
        return txCreate(
                walletPath,
                chainType,
                account,
                password,
                checkNodeApiHttpAddr,
                message,
                amount,
                selectionStrategyIsUseAll
        )
    }

}