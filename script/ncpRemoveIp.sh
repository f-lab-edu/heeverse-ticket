#!/bin/bash
function makeSignature() {

	nl=$'\\n'

	TIMESTAMP=$(echo $(($(date +%s%N)/1000000)))
	ACCESSKEY=$NCP_ACCESS_KEY
	SECRETKEY=$NCP_SECRET_KEY

	METHOD="GET"
	URI="/vserver/v2/removeAccessControlGroupInboundRule?regionCode=KR&vpcNo=$VPC_NO&accessControlGroupNo=$ACCESS_CONTROL_GROUP_NO&accessControlGroupRuleList.1.protocolTypeCode=TCP&accessControlGroupRuleList.1.ipBlock=$IP/32&accessControlGroupRuleList.1.portRange=8888"

	echo $IP

	SIG="$METHOD"' '"$URI"${nl}
	SIG+="$TIMESTAMP"${nl}
	SIG+="$ACCESSKEY"

	SIGNATURE=$(echo -n -e "$SIG"|iconv -t utf8 |openssl dgst -sha256 -hmac $SECRETKEY -binary|openssl enc -base64)


	responseCode=$(curl -s 'https://ncloud.apigw.ntruss.com'$URI \
		--header "Content-type:application/json" \
		--header "x-ncp-apigw-timestamp:$TIMESTAMP" \
		--header "x-ncp-iam-access-key:$ACCESSKEY" \
		--header "x-ncp-apigw-signature-v2:$SIGNATURE" | sed -n 's/.*<returnCode>\(.*\)<\/returnCode>.*/\1/p')

	if [ "$responseCode" -eq 0 ]; then
		echo 'Successfully done.'
	else
		echo 'Failed.'
		echo $responseCode
	fi


}

makeSignature
