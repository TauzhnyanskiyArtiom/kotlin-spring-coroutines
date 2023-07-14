package com.demo.coroutines.controller

import com.demo.coroutines.dto.UserRequest
import com.demo.coroutines.dto.UserResponse
import com.demo.coroutines.extension.toModel
import com.demo.coroutines.extension.toResponse
import com.demo.coroutines.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    suspend fun createUser(@RequestBody userRequest: UserRequest): UserResponse {
        return userService.saveUser(userRequest.toModel()).toResponse()
    }

    @GetMapping
    suspend fun findAllUsers(
        @RequestParam("name", required = false) name: String?
    ): Flow<UserResponse> {
        val users = name?.let {
            userService.findByNameLike(it)
        } ?: userService.findAllUsers()
        return users.map { it.toResponse() }
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable("id") id: Long): UserResponse {
        return userService.findById(id).toResponse()
    }

    @PutMapping("/{id}")
    suspend fun updateById(
        @PathVariable("id") id: Long,
        @RequestBody userRequest: UserRequest
    ) {
        userService.updateById(id, userRequest.toModel())
    }

    @DeleteMapping("/{id}")
    suspend fun deleteByID(@PathVariable("id") id: Long) {
        userService.deleteById(id)
    }

}