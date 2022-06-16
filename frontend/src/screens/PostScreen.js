import React, { useState } from 'react'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Error from '../components/Error'
import Loading from '../components/Loading'
import EditPost from '../components/posts/EditPost'
import PostCard from '../components/posts/PostCard'
import { getFollowingsPosts } from '../_actions/PostActions'

function PostScreen() {

    const auth = useSelector(state => state.auth)
    const posts = useSelector(state => state.posts)

    const dispatch = useDispatch()

    const [isCreate, setIsCreate] = useState(false)
    const [page, setPage] = useState(1)

    useEffect(() => {
        if(auth.user && auth.user.id){
            dispatch(getFollowingsPosts(1, auth.user.id))
        }
    }, [dispatch, auth.user])

    const handleLoadMore = () =>{
        dispatch(getFollowingsPosts(page+1, auth.user.id)).then(res=>{
            setPage(page+1)
        })
    }
    
    return (
        <>
        {
            posts.error &&posts.error.message && <Error error = {posts.error.message}/>
        }
        {
            posts.Loading && <Loading/>
        }

        {
            posts.loading ===false && 
            <div className="post_screen">
                <div className="form_button">
                    <i onClick={()=>setIsCreate(!isCreate)} className="far fa-comment-dots fa-4x" style={{cursor:'pointer', margin:"2rem"}}></i>
                </div>

                {
                     posts.posts.map((post, index)=>(
                        <PostCard post={post} key={index}/>
                    ))
                }
                {
                    isCreate &&
                    <EditPost isCreate={isCreate} setIsCreate={setIsCreate}/>
                }

                {
                    posts.totalPage>page &&
                    <div className="form_button">
                        <button onClick={handleLoadMore}>Load More</button>
                    </div>
                }


            </div>
        }
        
        </>
    )
}

export default PostScreen
