// @Title 
// @Description  无流通hcoin chaincode v1.0
// @Date 2018.5.10
// @Author disp

package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	sv "hmxq.top/bluestar/chaincode/hcoincc/services"
)

type LotusChaincode struct {
}

func (t *LotusChaincode) Init(APIstub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (t *LotusChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := stub.GetFunctionAndParameters()
	fmt.Println("Invoke is running " + function)

	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "UpdateCoin" {
		return sv.NewProvider().UpdateCoin(stub, args)
	} else if function == "QueryCoin" {
		return sv.NewProvider().QueryCoin(stub, args)
	} else if function == "RecycleCoin" {
		return sv.NewProvider().RecycleCoin(stub, args)
	} else if function == "TestMsg" {
		return sv.TestMsg(stub, args)
	} else if function == "TestTrace" {
		return sv.TestTrace(stub, args)
	}

	return shim.Error("Invalid Smart Contract function name.")

}

func main() {
	// Create a new Smart Contract
	err := shim.Start(new(LotusChaincode))
	if err != nil {
		fmt.Printf("Error starting Lotus chaincode: %s\n", err)
	}
}
