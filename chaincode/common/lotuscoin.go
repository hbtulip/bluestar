// @Title 
// @Description  接口定义
// @Date 2018.5.10
// @Author disp

package common

import (
	_ "encoding/json"
	"fmt"
	"strconv"
	"strings"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type LotusCoin struct {
	Label      string
	CoinAccess //匿名interface，实现类似纯虚函数功能
}

type CoinAccess interface {
	MakeKey(id string) string
	UpdateCoin(stub shim.ChaincodeStubInterface, args []string) pb.Response
	QueryCoin(stub shim.ChaincodeStubInterface, args []string) pb.Response
	RecycleCoin(stub shim.ChaincodeStubInterface, args []string) pb.Response
	CashOut(stub shim.ChaincodeStubInterface, args []string) pb.Response
}

func (c *LotusCoin) UpdateCoin(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	//User_ID Balance_s Remarks Reserve1
	if len(args) < 4 {
		return shim.Error("Incorrect number of arguments. ")
	}

	userid := strings.TrimSpace(args[0])
	if len(userid) < 1 {
		return shim.Error("Incorrect arguments. ")
	}

	balance_s := args[1]
	balance, err1 := strconv.ParseInt(balance_s, 10, 64) //strconv.Atoi(args[10])
	if err1 != nil {
		return shim.Error("Incorrect arguments. " + args[1] + "（余额转换失败）")
	}

	fmt.Printf("(UpdateCoin) k1=%s\n", userid)

	var hc CoinInfo
	hc.User_ID = userid
	hc.Balance = balance
	hc.Balance_s = balance_s
	hc.Remarks = args[2]
	hc.Reserve1 = args[3]
	hc.LastUpdateTime = time.Now().Format("2006-01-02 15:04:05")
	userInfoKey := c.MakeKey(userid)
	fmt.Printf("(UpdateCoin) k2=%s\n", userInfoKey)

	err := PutObjectToLedger(stub, userInfoKey, &hc)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success([]byte(LCS_SUCCESS))
}

func (c *LotusCoin) QueryCoin(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. ")
	}
	userid := strings.TrimSpace(args[0])
	if len(userid) < 1 {
		return shim.Error("Incorrect arguments. ")
	}

	fmt.Printf("(QueryCoin) k=%s\n", userid)
	bytes, err := stub.GetState(c.MakeKey(userid))
	if err != nil {
		return shim.Error(err.Error())
	}
	if bytes == nil {
		return shim.Error("User ID " + userid + " is not found in ledger")
	}

	return shim.Success(bytes)
}

func (c *LotusCoin) RecycleCoin(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. ")
	}
	userid := strings.TrimSpace(args[0])
	if len(userid) < 1 {
		return shim.Error("Incorrect arguments. ")
	}

	err := stub.DelState(c.MakeKey(userid))
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success([]byte(LCS_SUCCESS))
}

func (c *LotusCoin) CashOut(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	return shim.Error(LCS_UNSUPPORTED)

}
