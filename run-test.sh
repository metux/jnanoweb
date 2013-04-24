#!/bin/bash

HOSTNAME="localhost"
PORT="8088"

function fail() {
	echo "FAIL: $*" >&2
	exit 1
}

function callurl() {
	if curl -D - "http://$HOSTNAME:$PORT/$1" ; then
		echo "==> HTTP REQUEST OK"
		return 0
	else
		echo "==> HTTP REQUEST FAILED"
		return 1
	fi
}

killall clouddird
make || fail "build error"

./clouddird -c conf.example &

echo "sleeping to get server some startup time"
sleep 1

echo "Testing /"
callurl ""

TEST_USERS="enrico.weigelt@vnc.biz stefan.saenger@vnc.biz michael.rouba@vnc.biz foo@bar.org"

for i in $TEST_USERS ; do
	echo "==> Testing user attributes for $i"
	callurl "/userattr/$i"
done

for i in $TEST_USERS ; do
	echo "==> Testing address book for $i"
	callurl "/addrbook/$i"
done

for sender in $TEST_USERS ; do
	for receiver in $TEST_USERS ; do
		echo "==> Testing accessdb: may $sender post to $receiver ?"
		callurl "/accessdb/$sender/$receiver"
	done
done
