import { CREATE_POST_FAIL, CREATE_POST_REQUEST, CREATE_POST_SUCCESS, DELETE_POST_FAIL, DELETE_POST_REQUEST, DELETE_POST_SUCCESS, GET_DISCOVER_POSTS_FAIL, GET_DISCOVER_POSTS_REQUEST, GET_DISCOVER_POSTS_SUCCESS, GET_FOLLOWINGS_POSTS_FAIL, GET_FOLLOWINGS_POSTS_REQUEST, GET_FOLLOWINGS_POSTS_SUCCESS, GET_HOME_POST_FAIL, GET_HOME_POST_REQUEST, GET_HOME_POST_SUCCESS, GET_MORE_FOLLOWINGS_POSTS, GET_POST_FAIL, GET_POST_REQUEST, GET_POST_SUCCESS, GET_SEARCH_POST_FAIL, GET_SEARCH_POST_REQUEST, GET_SEARCH_POST_SUCCESS, LIKE_POST_FAIL, LIKE_POST_REQUEST, LIKE_POST_SUCCESS, UNLIKE_POST_FAIL, UNLIKE_POST_REQUEST, UNLIKE_POST_SUCCESS, UPDATE_POST_FAIL, UPDATE_POST_REQUEST, UPDATE_POST_SUCCESS } from "../_constants/PostConstants";

export const discoverReducer = (state={posts:[]}, action)=>{
    switch(action.type){
        case GET_DISCOVER_POSTS_REQUEST:
            return {...state, loading:true}
        case GET_DISCOVER_POSTS_SUCCESS:
            return {...state, loading:false, posts:[...state.posts,...action.payload.posts], totalElement:action.payload.totalElement, totalPage:action.payload.totalPage}
        case GET_DISCOVER_POSTS_FAIL:
            return {...state, loading:false, error:action.payload}
        default:
            return state;
    }
}

export const followingPostsReducer = (state={posts:[]}, action)=>{
    switch(action.type){
        case GET_FOLLOWINGS_POSTS_REQUEST:
            return {...state, loading:true}
        case GET_FOLLOWINGS_POSTS_SUCCESS:
            return {...state, loading:false, posts:action.payload.page===1? action.payload.posts :[...state.posts,...action.payload.posts], totalElement:action.payload.totalElement, totalPage:action.payload.totalPage}
        case GET_FOLLOWINGS_POSTS_FAIL:
            return {...state, loading:false, error:action.payload}
        
        // case GET_MORE_FOLLOWINGS_POSTS:
        //     return {...state, loading:false, posts:[...state.posts,...action.payload.posts], totalElement:action.payload.totalElement, totalPage:action.payload.totalPage}

        case CREATE_POST_REQUEST:
            return {...state, loading:true}
        case CREATE_POST_SUCCESS:
            return {...state, loading:false, posts:[action.payload,...state.posts]}
        case CREATE_POST_FAIL:
            return {...state, loading:false, error:action.payload}

        case UPDATE_POST_REQUEST:
            return {...state, loading:true}
        case UPDATE_POST_SUCCESS:
            return {...state, loading:false, posts:state.posts.map(post=>post.id === action.payload.id? action.payload : post)}
        case UPDATE_POST_FAIL:
            return {...state, loading:false, error:action.payload}
        
        case DELETE_POST_REQUEST:
            return {...state, loading:true}
        case DELETE_POST_SUCCESS:
            return {...state, loading:false, posts: state.posts.filter(post=>post.id !==action.payload)}
        case DELETE_POST_FAIL:
            return {...state, loading:false, error:action.payload}

        case LIKE_POST_REQUEST:
            return {...state, loading:true}
        case LIKE_POST_SUCCESS:
            return {...state, loading:false, posts:state.posts.map(post=> post.id===action.payload.postId ? {...post, postLikes:[...post.postLikes, action.payload.likePost]}: post)}
        case LIKE_POST_FAIL:
            return {...state, loading:false, error:action.payload}

        case UNLIKE_POST_REQUEST:
            return {...state, loading:true}
        case UNLIKE_POST_SUCCESS:
            return {...state, loading:false, posts:state.posts.map(post=> post.id===action.payload.postId ? {...post, postLikes:post.postLikes.filter(like=>like.id!==action.payload.likeId)}: post)}
        case UNLIKE_POST_FAIL:
            return {...state, loading:false, error:action.payload}

        default:
            return state;
    }
}

export const postReducer = (state={}, action)=>{
    switch(action.type){

        case GET_POST_REQUEST:
            return {...state, loading:true, error:""}
        case GET_POST_SUCCESS:
            return {...state, loading:false, post:action.payload, error:""}
        case GET_POST_FAIL:
            return {...state, loading:false, error:action.payload}  


        case UPDATE_POST_REQUEST:
            return {...state, loading:true}
        case UPDATE_POST_SUCCESS:
            return {...state, loading:false, posts:state.post?.id===action.payload.id && action.payload}
        case UPDATE_POST_FAIL:
            return {...state, loading:false, error:action.payload}      

        case LIKE_POST_REQUEST:
            return {...state, loading:true, error:""}
        case LIKE_POST_SUCCESS:
            return {...state, loading:false, post:state.post?.id===action.payload.postId && {...state.post, postLikes:[...state.post.postLikes, action.payload.likePost]}, error:""}
        case LIKE_POST_FAIL:
            return {...state, loading:false, error:action.payload}

        case UNLIKE_POST_REQUEST:
            return {...state, loading:true, error:""}
        case UNLIKE_POST_SUCCESS:
            return {...state, loading:false, post:state.post?.id===action.payload.postId && {...state.post, postLikes:state.post.postLikes.filter(like=>like.id!==action.payload.likeId)}, error:""}
        case UNLIKE_POST_FAIL:
            return {...state, loading:false, error:action.payload}
        
        default:
            return state;
    }
}

export const homeReducer = (state={page:1}, action)=>{
    switch(action.type){
    
        case GET_HOME_POST_REQUEST:
            return {...state, loading:true, error:""}
        case GET_HOME_POST_SUCCESS:
            return {...state, loading:false, posts:action.payload.page===1? action.payload.posts :[...state.posts,...action.payload.posts], totalElement:action.payload.totalElement, totalPage:action.payload.totalPage, error:""}
        case GET_HOME_POST_FAIL:
            return {...state, loading:false, error:action.payload}
        
        case GET_SEARCH_POST_REQUEST:
            return {...state, loading:true, error:""}
        case GET_SEARCH_POST_SUCCESS:
            return {...state, loading:false, posts:action.payload.page===1? action.payload.posts :[...state.posts,...action.payload.posts], totalElement:action.payload.totalElement, totalPage:action.payload.totalPage, error:""}
        case GET_SEARCH_POST_FAIL:
            return {...state, loading:false, error:action.payload}

        // case LIKE_POST_REQUEST:
        //     return {...state, loading:true, error:""}
        // case LIKE_POST_SUCCESS:
        //     return {...state, loading:false, posts:state.posts.map(post=>post.id===action.payload.postId ? {...post, postLikes:[...post.postLikes, action.payload.likePost]}:post), error:""}
        // case LIKE_POST_FAIL:
        //     return {...state, loading:false, error:action.payload}

        // case UNLIKE_POST_REQUEST:
        //     return {...state, loading:true, error:""}
        // case UNLIKE_POST_SUCCESS:
        //     return {...state, loading:false, posts:state.posts.map(post=>post.id===action.payload.postId? {...post, postLikes:post.postLikes.filter(like=>like.id!==action.payload.likeId)}:post), error:""}
        // case UNLIKE_POST_FAIL:
        //     return {...state, loading:false, error:action.payload}

        

        default:
            return state;
    }
}