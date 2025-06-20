/**
 * ***************************************************************************
 *    Copyright 2014-2021 Spectra Logic Corporation. All Rights Reserved.
 * ***************************************************************************
 */
package com.spectralogic.rioclient

import kotlinx.serialization.Serializable
import java.net.URI
import java.util.Collections.emptyMap
import java.util.UUID

enum class JobStatusEnum(
    val isFinal: Boolean = false,
) {
    ACTIVE,
    COMPLETED(true),
    CANCELED(true),
    ERROR(true),
    UNKNOWN,
    ;

    companion object {
        fun parse(status: String): JobStatusEnum =
            try {
                JobStatusEnum.valueOf(status)
            } catch (_: IllegalArgumentException) {
                UNKNOWN
            }
    }
}

enum class FileStatusEnum(
    val isFinal: Boolean = false,
) {
    Completed(true),
    Error(true),
    Initializing,
    Indexing,
    Copying,
    Transferring,
    Rewrapping,
    UNKNOWN,
    ;

    companion object {
        fun parse(status: String): FileStatusEnum =
            try {
                FileStatusEnum.valueOf(status)
            } catch (_: IllegalArgumentException) {
                UNKNOWN
            }
    }
}

@Serializable
data class JobStatus(
    val message: String,
    val status: String,
    val reason: String? = null,
)

@Serializable
data class JobCallback(
    val url: String,
    val eventClass: String,
    val eventType: String,
)

@Serializable
open class DetailedJobResponse(
    val name: String?,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val creationDate: String,
    val lastUpdated: String,
    val status: JobStatus,
    val jobType: JobType,
    val numberOfFiles: Long,
    val filesTransferred: Long,
    val totalSizeInBytes: Long,
    val progress: Float,
    val files: List<FileStatus>,
    val foreignJobs: Map<
        @Serializable(with = UUIDSerializer::class)
        UUID,
        ForeignJobDetails,
    > = emptyMap(),
    val priority: String? = null,
    val callbacks: List<JobCallback>? = null,
) : RioResponse()

@Serializable
open class JobResponse(
    val name: String?,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val creationDate: String,
    val lastUpdated: String,
    val status: JobStatus,
    val jobType: JobType,
    val numberOfFiles: Long,
    val filesTransferred: Long,
    val totalSizeInBytes: Long,
    val progress: Float,
    val foreignJobs: Map<
        @Serializable(with = UUIDSerializer::class)
        UUID,
        ForeignJobDetails,
    > = emptyMap(),
    val priority: String? = null,
    val sessionId: String? = null,
    val callbacks: List<JobCallback>? = null,
) : RioResponse()

@Serializable
data class JobData(
    val name: String?,
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val creationDate: String,
    val lastUpdated: String,
    val status: JobStatus,
    val jobType: JobType,
    val numberOfFiles: Long,
    val filesTransferred: Long,
    val totalSizeInBytes: Long,
    val progress: Float,
    val foreignJobs: Map<
        @Serializable(with = UUIDSerializer::class)
        UUID,
        ForeignJobDetails,
    > = emptyMap(),
    val priority: String? = null,
    val callbacks: List<JobCallback>? = null,
)

@Serializable
enum class JobType {
    ARCHIVE,
    RESTORE,
    COPY,
    DELETE,
}

@Serializable
data class ForeignJobDetails(
    val id: String,
    val type: String,
)

@Serializable
data class JobListResponse(
    val jobs: List<JobData>,
    val page: PageInfo,
) : RioResponse(),
    RioListResponse<JobData> {
    override fun page() = page

    override fun results() = jobs
}

@Serializable
data class FileStatus(
    val name: String,
    val status: String,
    val statusMessage: String,
    @Serializable(with = UUIDSerializer::class)
    val foreignJob: UUID? = null,
    @Serializable(with = URISerializer::class)
    val uri: URI,
    val sizeInBytes: Long,
    val lastUpdated: String,
    val broker: String?,
    val agent: String?,
    val fileId: String,
)

@Serializable
data class FileStatusLogResponse(
    val page: PageInfo,
    val fileStatus: List<FileStatusResponse>,
) : RioResponse()

@Serializable
data class FileStatusResponse(
    val name: String,
    @Serializable(with = URISerializer::class)
    val uri: URI,
    val sizeInBytes: Long,
    val status: String,
    val statusMessage: String,
    val lastUpdated: String,
    @Serializable(with = UUIDSerializer::class)
    val foreignJob: UUID?,
) : RioResponse()

@Serializable
data class ArchiveFolderResponse(
    val jobGroupId: String,
) : RioResponse()

@Serializable
data class JobGroupRequest(
    val groupName: String,
    val groupType: String,
) : RioRequest

@Serializable
data class JobGroupResponse(
    val groupName: String,
    val groupId: String,
    val groupType: String,
    val errorCount: Int,
    val jobGroupStatus: JobStatus,
    val createBy: String,
    val createDate: Long,
    val updateBy: String,
    val updateDate: Long,
) : RioResponse()

@Serializable
data class JobGroupListResponse(
    val jobGroups: List<JobGroupResponse>,
    val page: PageInfo,
) : RioResponse(),
    RioListResponse<JobGroupResponse> {
    override fun page() = page

    override fun results() = jobGroups
}

@Serializable
data class JobGroupStatusResponse(
    val groupName: String,
    val summary: String,
    val errorCount: Int,
    val jobGroupStatus: JobStatus,
    val createBy: String,
    val createDate: Long,
    val updateBy: String,
    val updateDate: Long,
    val jobs: List<DetailedJobResponse>,
    val failedFiles: List<JobGroupFailedFile>,
) : RioResponse()

@Serializable
data class JobGroupFailedFile(
    val uri: String,
    val statusMessage: String,
)
