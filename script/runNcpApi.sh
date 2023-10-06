#!/bin/bash
function makeSignature() {

	nl=$'\\n'

	TIMESTAMP=$(echo $(($(date +%s%N)/1000000)))
	ACCESSKEY=$NCP_ACCESS_KEY
	SECRETKEY=$NCP_SECRET_KEY
	NCP_SERVER_API_PATH=$API_PATH
	NGRINDER_AGENT=$NGRINDER_AGENT_INSTANCE_ID
	NGRINDER_CONTROLLER=$NGRINDER_CONTROLLER_INSTANCE_ID

	METHOD="GET"
	URI="/server/v2/$NCP_SERVER_API_PATH?serverInstanceNoList.1=$NGRINDER_AGENT&serverInstanceNoList.2=$NGRINDER_CONTROLLER"

	SIG="$METHOD"' '"$URI"${nl}
	SIG+="$TIMESTAMP"${nl}
	SIG+="$ACCESSKEY"

	SIGNATURE=$(echo -n -e "$SIG"|iconv -t utf8 |openssl dgst -sha256 -hmac $SECRETKEY -binary|openssl enc -base64)


	curl -s 'https://ncloud.apigw.ntruss.com'$URI \
		--header "Content-type:application/json" \
		--header "x-ncp-apigw-timestamp:$TIMESTAMP" \
		--header "x-ncp-iam-access-key:$ACCESSKEY" \
		--header "x-ncp-apigw-signature-v2:$SIGNATURE | sed -n 's/.*<returnCode>\(.*\)<\/returnCode>.*/\1/p'"

}

makeSignature
