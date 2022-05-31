import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Error from '../components/Error'
import Loading from '../components/Loading'
import PostCard from '../components/posts/PostCard'
import { getPost } from '../_actions/PostActions'

function PostDetailScreen(props) {

    const id = props.match.params.id

    const post = useSelector(state => state.post)

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getPost(id))
    }, [dispatch, id])
    return (
        <div className="post_screen">
            {
                post.error && post.error.message && <Error error = {post.error.message}/>
            }
            {
                post.Loading && <Loading/>
            }

            {
                (post.loading ===false && post.post) &&
                    <PostCard post={post.post}/>

            }
        </div>
    )
}

export default PostDetailScreen
