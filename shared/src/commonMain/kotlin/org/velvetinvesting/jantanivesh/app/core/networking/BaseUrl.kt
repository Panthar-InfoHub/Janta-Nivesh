package org.velvetinvesting.jantanivesh.app.core.networking

const val BASE_URL= "https://janta-nivesh-backend-34918043640.asia-south1.run.app/api/v1"

const val PROD_URL="https://prod-velvet-357888765640.asia-south1.run.app/api/v1"

fun getUrl(endPoint:String)= "$BASE_URL$endPoint"