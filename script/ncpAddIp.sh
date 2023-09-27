#!/bin/bash
function makeSignature() {

	nl=$'\\n'

	TIMESTAMP=$(echo $(($(date +%s%N)/1000000)))
	ACCESSKEY=$NCP_ACCESS_KEY
	SECRETKEY=$NCP_SECRET_KEY

	METHOD="GET"
	URI="/vserver/v2/addAccessControlGroupInboundRule?regionCode=KR&vpcNo=$VPC_NO&accessControlGroupNo=$ACCESS_CONTROL_GROUP_NO&accessControlGroupRuleList.1.protocolTypeCode=TCP&accessControlGroupRuleList.1.ipBlock=$IP/32&accessControlGroupRuleList.1.portRange=$PORT"

	echo $IP

	SIG="$METHOD"' '"$URI"${nl}
	SIG+="$TIMESTAMP"${nl}
	SIG+="$ACCESSKEY"

	SIGNATURE=$(echo -n -e "$SIG"|iconv -t utf8 |openssl dgst -sha256 -hmac $SECRETKEY -binary|openssl enc -base64)


	curl --location 'https://ncloud.apigw.ntruss.com'$URI \
		--header "Content-type:application/json" \
		--header "x-ncp-apigw-timestamp:$TIMESTAMP" \
		--header "x-ncp-iam-access-key:$ACCESSKEY" \
		--header "x-ncp-apigw-signature-v2:$SIGNATURE"

}

makeSignature