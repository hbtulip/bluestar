// @Title 
// @Description  主链码lotus chaincode v1.0
// @Date 2018.5.10
// @Author disp

package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	sv "hmxq.top/bluestar/chaincode/lotuscc/services"
)

type LotusChaincode  struct{
}

/*
// Chaincode interface must be implemented by all chaincodes.
// The fabric runs the transactions by calling these functions as specified.
type Chaincode interface {
	// Init is called during Instantiate transaction after the chaincode container
	// has been established for the first time, allowing the chaincode to initialize its internal data.
	Init(stub ChaincodeStubInterface) pb.Response

	// Invoke is called to update or query the ledger in a proposal transaction.
	// Updated state variables are not committed to the ledger until the transaction is committed.
	Invoke(stub ChaincodeStubInterface) pb.Response
}
*/

func (t *LotusChaincode) Init(APIstub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (t *LotusChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := stub.GetFunctionAndParameters()
	fmt.Println("Invoke is running " + function)

	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "RegisterUser" { 
		return sv.RegisterUser(stub, args)
	} else if function == "UpdateUserinfo" { 
		return sv.UpdateUserinfo(stub, args)
	} else if function == "QueryUserinfo" { 
		return sv.QueryUserinfo(stub, args)
	} else if function == "QueryUserinfoEx" { 
			return sv.QueryUserinfoEx(stub, args)		
	} else if function == "UnregisterUser" { 
			return sv.UnregisterUser(stub, args)
	} else if function == "TestMsg" {
		return sv.TestMsg(stub, args)
	} else if function == "TestInvokeCC" {
		return sv.TestInvokeCC(stub, args)
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
