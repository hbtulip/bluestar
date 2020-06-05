package encrypt

import (
	"bytes"
	"encoding/base64"
	"fmt"
	"strings"
	"testing"
)

func TestAes(t *testing.T) {

	fmt.Println("TestAes...")

	//数据分组长度为128bit、密钥长度为128/192/256bit
	var aeskey = []byte("321423u9y8d2fwfl")
	pass := []byte("这是待加密的明文=,A123vdncloud_123456")
	xpass, err := AesEncrypt(pass, aeskey)
	if err != nil {
		t.Errorf(err.Error())
		t.Fail()
	}

	pass64 := base64.StdEncoding.EncodeToString(xpass)
	fmt.Printf("加密后:%v\n", pass64)

	bytesPass, err := base64.StdEncoding.DecodeString(pass64)
	if err != nil {
		t.Errorf(err.Error())
		t.Fail()
	}

	tpass, err := AesDecrypt(bytesPass, aeskey)
	if err != nil {
		t.Errorf(err.Error())
		t.Fail()
	}
	fmt.Printf("解密后:%s\n", tpass)

	if bytes.Compare(pass, tpass) != 0 {
		t.Errorf(err.Error())
		t.Fail()
	}
}

func TestDes(t *testing.T) {
	fmt.Println("TestDes...")

	//明文按64位进行分组，密钥长64bit(事实上是56bit参与)
	var key = "12345678"
	var info = "这是待加密的明文=,A123vdncloud_123456"

	Enc_str := EncryptDES_CBC(info, key)
	fmt.Println(Enc_str)
	Dec_str := DecryptDES_CBC(Enc_str, key)
	//fmt.Println(Dec_str)

	if strings.Compare(info, Dec_str) != 0 {
		t.Fail()
	}

	Enc_str = EncryptDES_ECB(info, key)
	fmt.Println(Enc_str)
	Dec_str = DecryptDES_ECB(Enc_str, key)
	//fmt.Println(Dec_str)
	if strings.Compare(info, Dec_str) != 0 {
		t.Fail()
	}
}

func TestMd5(t *testing.T) {
	fmt.Println("TestMd5...")
}
