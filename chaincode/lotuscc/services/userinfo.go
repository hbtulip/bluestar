package services

import (
	"encoding/json"
	_ "errors"
	_ "strconv"
	"fmt"
	"time"
	"strings"
	"hmxq.top/bluestar/chaincode/common"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func RegisterUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) < 6 {
		return shim.Error("Incorrect number of arguments. ")
	}

	userid := strings.TrimSpace(args[0])
	if len(userid) < 1 {
		return shim.Error("Incorrect arguments. ")
	}

	//跳过检查用户是否存在

	var userInfo common.Userinfo
	userInfo.User_ID = userid
	userInfo.Name = args[1]
	userInfo.Phone = args[2]
	userInfo.CreateTime = args[3]
	userInfo.Remarks = args[4]
	userInfo.Reserve1 = args[5]
	userInfo.LastUpdateTime = time.Now().Format("2006-01-02 15:04:05")

	userInfoKey := common.GetUserinfoKey(userid)

	err := common.PutObjectToLedger(stub, userInfoKey, &userInfo)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success([]byte(common.LCS_SUCCESS))
}

func UpdateUserinfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	//检查用户是否存在
	var userInfo common.Userinfo
	userInfo.User_ID = strings.TrimSpace(args[0])
	userInfoKey := common.GetUserinfoKey(userInfo.User_ID)
	err := common.GetObjectFromLedger(stub, userInfoKey, &userInfo)
	if err != nil {
		return shim.Error(err.Error())
	}

	//更新用户信息
	return RegisterUser(stub, args)
}

func QueryUserinfo(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	userid := args[0]
	bytes, err := stub.GetState(common.GetUserinfoKey(userid))
	if err != nil {
		return shim.Error(err.Error())
	}
	if bytes == nil {
		return shim.Error("User ID " + userid + " is not found in ledger")
	}

	return shim.Success(bytes)
}

func getUserCoinBalance(stub shim.ChaincodeStubInterface, userid string, cointype int) string {
	invokeArgs := common.MakeChaincodeArgs("QueryCoin",userid)
	var response pb.Response
	if cointype == 0 {
		//chaincodeName string, args [][]byte, channel string)
		response = stub.InvokeChaincode(common.HF_BCOINCC, invokeArgs, common.HF_CHANNEL)
	} else {
		//chaincodeName string, args [][]byte, channel string)
		response = stub.InvokeChaincode(common.HF_HCOINCC, invokeArgs, common.HF_CHANNEL)
	}

	coinBalance := "n/a"
	if response.Status == shim.OK {
		fmt.Printf("Invoke %s successful. Got response [%s]\n", common.HF_BCOINCC, string(response.Payload))
		//return shim.Success(response.Payload)
		//解析出 balance_s
		maptmp := make(map[string]interface{})
		err := json.Unmarshal(response.Payload, &maptmp)
		if err != nil {
			fmt.Println(err)
		}else{
			//必须先判断是否为空，然后再转换类型
			if maptmp["balance_s"] != nil {
				coinBalance = maptmp["balance_s"].(string)	
			}
		}

	} else {
		errStr := fmt.Sprintf("Failed to invoke %s. Got error: %s\n", common.HF_BCOINCC, response.GetMessage())
		fmt.Printf(errStr)
		//return shim.Error(errStr)
	}

	return coinBalance
}

func QueryUserinfoEx(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	userid := args[0]
	bytes, err := stub.GetState(common.GetUserinfoKey(userid))
	if err != nil {
		return shim.Error(err.Error())
	}
	if bytes == nil {
		return shim.Error("User ID " + userid + " is not found in ledger")
	}

	//调用链码，获取BCOIN余额
	bcoinBalance := getUserCoinBalance(stub, userid, 0)
	fmt.Printf("bcoinBalance=%s\n", bcoinBalance)

	//调用链码，获取HCOIN余额
	hcoinBalance := "n/a" //getUserCoinBalance(stub, userid, 1)
	fmt.Printf("hcoinBalance=%s\n", hcoinBalance)

	maptmp := make(map[string]interface{})
    err = json.Unmarshal(bytes, &maptmp)
    if err != nil {
		fmt.Println(err)
		return shim.Error(err.Error())
	}
	maptmp["BCoinBalance_s"] = bcoinBalance
	maptmp["HCoinBalance_s"] = hcoinBalance	
	bytes, err = json.Marshal(maptmp)
    if err != nil {
		fmt.Println(err)
		return shim.Error(err.Error())
	}

	return shim.Success(bytes)
}

func UnregisterUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//检查输入参数
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. ")
	}
	userid := strings.TrimSpace(args[0])
	if len(userid) < 1 {
		return shim.Error("Incorrect arguments. ")
	}

	//检查当前用户是否存在
	var userInfo common.Userinfo
	userInfo.User_ID = userid
	userInfoKey := common.GetUserinfoKey(userInfo.User_ID)
	err := common.GetObjectFromLedger(stub, userInfoKey, &userInfo)
	if err != nil {
		return shim.Error(err.Error())
	}

	//调用跨链码查询，检查hcoin/bcoin的余额是否为空

	//删除其他链码上对应内容

	//注销用户
	err2 := stub.DelState(userInfoKey)
	if err2 != nil {
		return shim.Error(err2.Error())
	}

	return shim.Success([]byte(common.LCS_SUCCESS))

}
