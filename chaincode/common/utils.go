// @Title 
// @Description  智能合约工具类
// @Date 2018.5.10
// @Author disp

 package common

import (
	"bytes"
	"encoding/json"
	"errors"
	"strings"
	//"strconv"
	//"time"
	//"unsafe"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

func GetUserinfoKey(userid string) string {
	return LC_USERINFO + strings.TrimSpace(userid)
}

func GetMempointKey(userid string) string {
	return LC_MEMPOINT + strings.TrimSpace(userid)
}

func GetObjectFromLedger(stub shim.ChaincodeStubInterface, key string, obj interface{}) error {
	bytes, err := stub.GetState(key)
	if err != nil {
		return err
	}
	if bytes == nil {
		return errors.New("key " + key + " is NOT FOUND in ledger")
	}
	err = json.Unmarshal(bytes, obj) //反序列化 json -> struct
	if err != nil {
		return err
	}
	return nil
}

//找不到记录不返回error，而是返回nil。适用于找不到记录也不需要报错的场景。
func GetObjectFromLedgerNotFound(stub shim.ChaincodeStubInterface, key string, obj interface{}) ([]byte, error) {
	bytes, err := stub.GetState(key)
	if err != nil {
		return nil, err
	}
	if bytes == nil {
		fmt.Printf("key: %s is not found in ledger", key)
		return nil, nil
	}
	err = json.Unmarshal(bytes, obj) //反序列化 json -> struct
	if err != nil {
		return nil, err
	}
	return bytes, nil
}

func PutObjectToLedger(stub shim.ChaincodeStubInterface, key string, obj interface{}) error {
	//序列化 struct -> json
	bytes, err := json.Marshal(obj)
	if err != nil {
		return err
	}

	//data := *(*[]byte)(unsafe.Pointer(&obj))
	err = stub.PutState(key, bytes)
	if err != nil {
		return err
	}
	return nil
}

func PutObjectToLedger2(stub shim.ChaincodeStubInterface, key string, obj interface{}) ([]byte, error) {
	//序列化 struct -> json
	bytes, err := json.Marshal(obj)
	if err != nil {
		return bytes, err
	}

	//data := *(*[]byte)(unsafe.Pointer(&obj))
	err = stub.PutState(key, bytes)
	if err != nil {
		return bytes, err
	}
	return bytes, nil
}

func MakeChaincodeArgs(args ...string) [][]byte {
	bargs := make([][]byte, len(args))
	for i, arg := range args {
		bargs[i] = []byte(arg)
	}
	return bargs
}

//溯源
func GetTraceFromLedger(stub shim.ChaincodeStubInterface, key string, obj interface{}) ([]byte, error) {

	it, err1 := stub.GetHistoryForKey(key)
	if err1 != nil {
		return nil, err1
	}

	var result, err2 = getHistoryListResult(it)
	return result, err2
}

func getHistoryListResult(resultsIterator shim.HistoryQueryIteratorInterface) ([]byte, error) {

	defer resultsIterator.Close()
	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		//包含字段
		//queryResponse.GetTxId() //"tx_id"
		//queryResponse.GetValue()
		//queryResponse.GetTimestamp()
		//queryResponse.GetIsDelete()

		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		item, _ := json.Marshal(queryResponse)
		buffer.Write(item)
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")
	//fmt.Printf("queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil
}
