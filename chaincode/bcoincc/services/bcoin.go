package services

import (
	"fmt"
	"strconv"
	"time"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"strings"
	"hmxq.top/bluestar/chaincode/common"
)

//通过组合(composite)实现类似继承(extends)和重写(override)的功能
type BCoin struct {
	common.LotusCoin //匿名struct实现类似继承功能
}

//重写方法 MakeKey
func (bc *BCoin) MakeKey(id string) string {
	return common.LC_BCOIN + strings.TrimSpace(id)
}

//重写方法 CashOut
func (bc *BCoin) CashOut(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//user_id,amount
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. ")
	}
	
	//判断用户是否存在
	var ci common.CoinInfo
	userid := args[0]
	err := common.GetObjectFromLedger(stub,bc.MakeKey(userid),&ci)
	if err != nil {
		return shim.Error(err.Error())
	}
	amount, err1 := strconv.ParseInt(args[1], 10, 64) 
	if err1 != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("amount=%d\n", amount)

	//判断提现金额
	if amount > 0 && amount <= ci.Balance {
		ci.Balance -= amount;
		ci.Balance_s = strconv.FormatInt(ci.Balance,10)
		ci.Remarks = fmt.Sprintf("CashOut,amount=%d",amount)
		ci.LastUpdateTime = time.Now().Format("2006-01-02 15:04:05")
		err = common.PutObjectToLedger(stub, bc.MakeKey(userid), &ci)
		if err != nil {
			return shim.Error(err.Error())
		}
	
		return shim.Success([]byte(common.LCS_SUCCESS))
	}

	return shim.Error(common.LCS_UNSUPPORTED)
}

//实例化
func NewProvider() common.CoinAccess {
	fmt.Printf("NewProvider()\n")

	//return &HCoin{common.LotusCoin{Label: "bcoin"}}
	var bc = BCoin{common.LotusCoin{Label: "bcoin"}}
	//避免重写无效，nil pointer dereference，golang本身的问题
	bc.CoinAccess = &bc
	return &bc
}
