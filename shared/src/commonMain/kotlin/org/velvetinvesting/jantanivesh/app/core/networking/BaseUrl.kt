package org.velvetinvesting.jantanivesh.app.core.networking

const val PROD_URL= "https://vlv-jn-001-34918043640.asia-south1.run.app/api/v2"

const val DEV_URL="https://velvet-v2-dev-34918043640.asia-south1.run.app/api/v2"

const val FD_URL="https://vlv-jn-001-34918043640.asia-south1.run.app/api/v1"

fun getUrl(endPoint:String)= "$DEV_URL$endPoint"

fun getFDUrl(endPoint:String)= "$FD_URL$endPoint"