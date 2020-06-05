package services

import (
	_ "encoding/json"
	_ "errors"
	"fmt"
	//"time"
	//"strconv"
	//"strings"
	"hmxq.top/bluestar/chaincode/common"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func TestMsg(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("(TestMsg) Hello World!\n")
	payload := "Lotus spreads aromatic wherever it grows." + args[0]
	return shim.Success([]byte(payload))
}

func TestInvokeCC(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("(TestInvokeCC) Hello World!\n")

	invokeArgs := common.MakeChaincodeArgs(args[2], args[3])
	//chaincodeName string, args [][]byte, channel string)
	response := stub.InvokeChaincode(args[1], invokeArgs, args[0])

	/*
		if response.Status != shim.OK {
			errStr := fmt.Sprintf("Failed to invoke chaincode. Got error: %s",  response.GetMessage())
			fmt.Printf(errStr)
			return shim.Error(errStr)
		}
		fmt.Printf("Invoke chaincode successful. Got response %s", string(response.Payload))
		return shim.Success(response.Payload)
	*/
	
	return response
}
