/**
 * ***************************************************************************
 *    Copyright 2014-2024 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
package com.spectralogic.rioclient

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    val username: String,
    val fullName: String,
    val password: String,
    val active: Boolean,
    val local: Boolean,
    val role: String,
) : RioRequest

@Serializable
data class UserUpdateRequest(
    val active: Boolean,
    val role: String,
    val fullName: String? = null,
) : RioRequest

@Serializable
data class UserUpdatePasswordRequest(
    val password: String,
) : RioRequest

@Serializable
data class UserListResponse(
    val users: List<UserResponse>,
    val page: PageInfo,
) : RioResponse(),
    RioListResponse<UserResponse> {
    override fun page() = page

    override fun results() = users
}

@Serializable
data class UserResponse(
    val username: String,
    val fullName: String,
    val active: Boolean,
    val local: Boolean,
    val role: String,
    val createDate: String,
    val updateDate: String,
) : RioResponse()
