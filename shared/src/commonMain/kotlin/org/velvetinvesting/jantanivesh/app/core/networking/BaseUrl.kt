package org.velvetinvesting.jantanivesh.app.core.networking

const val BASE_URL= "https://vlv-jn-001-34918043640.asia-south1.run.app/api/v1"

const val PROD_URL="https://velvet-v2-dev-34918043640.asia-south1.run.app/api/v2"

fun getUrl(endPoint:String)= "$PROD_URL$endPoint"