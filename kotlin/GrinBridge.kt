package net.vite.wallet.grin

import io.reactivex.Observable
import net.vite.wallet.network.applyIoScheduler
import java.io.File

/**
 * init, then call the function named end with 'KT'
 *
 * Docs at https://github.com/vitelabs/Vite_GrinWallet/tree/8b08aa50fdb8bf5152747b0ce4271fa352822c0c
 */
class GrinBridge private constructor() {

    private object Holder {
        var instance = GrinBridge()
    }

    companion object {

        init {
            System.loadLibrary("wallet")
        }

        fun get(
                chainType: String,
                walletPath: String,
                password: String,
                checkNodeApiHttpAddr: String,
                apiSecret: String
        ): GrinBridge {
            Holder.instance.init(chainType, walletPath, password, checkNodeApiHttpAddr, apiSecret)
            return Holder.instance
        }
    }


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

    fun walletInfo(
            refreshFromNode: Boolean
    ): Observable<ResultType> {
        return Observable.fromCallable {
            val balance = balance(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    refreshFromNode
            )
            handleCResult(balance)
        }.applyIoScheduler()
    }

    private external fun txsGet(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean
    ): String

    fun txsGetKT(
            refreshFromNode: Boolean
    ): Observable<ResultType> {
        return Observable.fromCallable {
            val txsGet = txsGet(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    refreshFromNode
            )
            handleCResult(txsGet)
        }.applyIoScheduler()
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


    fun txGetKT(
            refreshFromNode: Boolean,
            txId: Int
    ): Observable<ResultType> {

        return Observable.fromCallable {
            val tx = txGet(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    refreshFromNode, txId
            )
            handleCResult(tx)
        }.applyIoScheduler()
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

    fun txCreateKT(
            message: String, amount: Long, selectionStrategyIsUseAll: Boolean
    ): Observable<ResultType> {
        return Observable.fromCallable {
            val result = txCreate(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    message,
                    amount,
                    selectionStrategyIsUseAll
            )
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun txStrategies(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            amount: Long
    ): String

    fun txStrategiesKT(
            amount: Long
    ): Observable<ResultType> {
        return Observable.fromCallable {
            val result = txStrategies(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    amount
            )
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun txCancel(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            id: Int
    ): String

    fun txCancelKT(id: Int): Observable<ResultType> {

        return Observable.fromCallable {
            val result = txCancel(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    id
            )
            handleCResult(result)
        }.applyIoScheduler()
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
            message: String
    ): Observable<ResultType> {

        return Observable.fromCallable {
            val result = txReceive(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    slatePath, message
            )
            handleCResult(result)
        }.applyIoScheduler()

    }

    private external fun txFinalize(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            slate_path: String
    ): String

    fun txFinalizeKT(slatePath: String): Observable<ResultType> {

        return Observable.fromCallable {
            val result = txFinalize(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    slatePath
            )
            handleCResult(result)
        }.applyIoScheduler()
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
    ): Observable<ResultType> {

        return Observable.fromCallable {
            val result = txSend(
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
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun txRepost(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            tx_slate_id: String
    ): String

    fun txRepostKT(
            txSlateId: String
    ): Observable<ResultType> {

        return Observable.fromCallable {
            val result = txRepost(
                    walletPath,
                    chainType,
                    account,
                    password,
                    checkNodeApiHttpAddr,
                    txSlateId
            )
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun walletInit(
            j_recipient: String,
            j_chain_type: String,
            j_password: String,
            j_check_node_api_http_addr: String
    ): String

    fun walletInitKT(): Observable<ResultType> {
        return Observable.fromCallable {
            val result = walletInit(
                    walletPath,
                    chainType,
                    password,
                    checkNodeApiHttpAddr
            )
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun walletPhrase(
            j_path: String,
            j_chain_type: String,
            j_password: String,
            j_check_node_api_http_addr: String
    ): String

    fun walletPhraseKT(): Observable<ResultType> {

        return Observable.fromCallable {
            val result = walletPhrase(
                    walletPath,
                    chainType,
                    password,
                    checkNodeApiHttpAddr
            )
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun walletRecovery(
            path: String,
            chain_type: String,
            phrase: String,
            password: String,
            check_node_api_http_addr: String
    ): String


    fun walletRecoveryKT(phrase: String): Observable<ResultType> {
        return Observable.fromCallable {
            val result =
                    walletRecovery(walletPath, chainType, phrase, password, checkNodeApiHttpAddr)
            handleCResult(result)
        }.applyIoScheduler()

    }

    private external fun walletCheck(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    fun walletCheckKT(): Observable<ResultType> {
        return Observable.fromCallable {
            val result =
                    walletCheck(walletPath, chainType, account, password, checkNodeApiHttpAddr)
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun walletRestore(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    fun walletRestoreKT(): Observable<ResultType> {
        return Observable.fromCallable {
            val result =
                    walletRestore(walletPath, chainType, account, password, checkNodeApiHttpAddr)
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun height(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String
    ): String

    fun heightKT(): Observable<ResultType> {
        return Observable.fromCallable {
            val result =
                    height(
                            walletPath,
                            chainType,
                            account,
                            password,
                            checkNodeApiHttpAddr
                    )
            handleCResult(result)
        }.applyIoScheduler()
    }

    private external fun outputsGet(
            path: String,
            chain_type: String,
            account: String,
            password: String,
            check_node_api_http_addr: String,
            refresh_from_node: Boolean
    ): String

    fun outputsGetKT(
            refreshFromNode: Boolean
    ): Observable<ResultType> {

        return Observable.fromCallable {
            val result =
                    outputsGet(
                            walletPath,
                            chainType,
                            account,
                            password,
                            checkNodeApiHttpAddr,
                            refreshFromNode
                    )
            handleCResult(result)
        }.applyIoScheduler()
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

    fun outputGetKT(
            refreshFromNode: Boolean,
            txId: Int
    ): Observable<ResultType> {
        return Observable.fromCallable {
            val result =
                    outputGet(
                            walletPath,
                            chainType,
                            account,
                            password,
                            checkNodeApiHttpAddr,
                            refreshFromNode,
                            txId
                    )
            handleCResult(result)
        }.applyIoScheduler()

    }


//    fun isResponseSlate(slatePath: String): String {
//        return slatePath.components(separatedBy: ".").last == "response" || slatePath.contains("response")
//        return ""
//    }
//
//    fun getSlateUrl(slateId: String, isResponse: Boolean): String {
//        let path = "\(walletUrl.path)/slates/\(slateId).grinslate\(isResponse ? ".response" : "")"
//        return URL(fileURLWithPath: path)
//        return ""
//    }

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

    external fun hello(
            j_recipient: String
    ): String

}