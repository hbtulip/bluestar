package services

import (
	_ "encoding/json"
	_ "errors"
	"fmt"
	//"time"
	//"strconv"
	"strings"
	"hmxq.top/bluestar/chaincode/common"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func TestMsg(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("(TestMsg) Hello World!\n")

	payload := "[bcoin]Lotus spreads aromatic wherever it grows." + args[0]
	return shim.Success([]byte(payload))
}

func TestTrace(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. ")
	}
	userid := strings.TrimSpace(args[0])
	if len(userid) < 1 {
		return shim.Error("Incorrect arguments. ")
	}
	fmt.Printf("(TestTrace) %s\n", userid)

	ai := NewProvider()
	result, err := common.GetTraceFromLedger(stub, ai.MakeKey(userid), ai)
	if err != nil {
		return shim.Error(err.Error())
	} else {
		return shim.Success(result)
	}

	/*
		[
		{
			tx_id: "852a3f11af23874bfab9779de01a88ccd411b6336cc9d84d354c6ab42965f39e",
			value: "eyJ1c2VyX2lkIjoiQTEwMDEiLCJiYWxhbmNlIjo1MDUyMCwiYmFsYW5jZV9zIjoiNTA1MjAiLCJyZW1hcmtzIjoiYWExMTEiLCJyZXNlcnZlMSI6ImJiMjIyMzMzIiwibGFzdHVwZGF0ZXRpbWUiOiIyMDE5LTA1LTE3IDE1OjQ3OjQ4In0=",
			timestamp: {
			seconds: 1558079268,
			nanos: 836305656
			}
		},
		{
			tx_id: "d3ce49b2e6016bf726dfcebf30d1db635d8621a920812b4d8f647a47cfbd3613",
			value: "eyJ1c2VyX2lkIjoiQTEwMDEiLCJiYWxhbmNlIjo1MjAsImJhbGFuY2VfcyI6IjUyMCIsInJlbWFya3MiOiJhYTExMSIsInJlc2VydmUxIjoiYmIyMjIzMzMiLCJsYXN0dXBkYXRldGltZSI6IjIwMTktMDUtMTcgMTU6NDg6MTIifQ==",
			timestamp: {
			seconds: 1558079292,
			nanos: 527098699
			}
		},
		{
			tx_id: "bd556f69db543b8f59d68c922f9def87609116134427ee433aae435ccce3b28e",
			value: "eyJ1c2VyX2lkIjoiQTEwMDEiLCJiYWxhbmNlIjo5NTIwLCJiYWxhbmNlX3MiOiI5NTIwIiwicmVtYXJrcyI6ImFheHgxMTEiLCJyZXNlcnZlMSI6Inl5eWJiMjIyMzMzIiwibGFzdHVwZGF0ZXRpbWUiOiIyMDE5LTA1LTE3IDE1OjQ4OjUxIn0=",
			timestamp: {
			seconds: 1558079331,
			nanos: 233350052
			}
		}
		,{
			"tx_id":"8c6d8469cd67ba210169f7fc02aca07ae984d74a39c653f7058e86f9fb8668c5",
			"timestamp":{"seconds":1558344561,"nanos":142693023},
			"is_delete":true
		}

		]
	*/

}
