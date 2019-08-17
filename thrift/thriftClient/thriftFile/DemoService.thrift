namespace java paxi.maokitty.verify.service

include 'myException.thrift'

typedef myException.myException myException

service DemoService{
 string say(1:string msg)throws(1:myException e),
}