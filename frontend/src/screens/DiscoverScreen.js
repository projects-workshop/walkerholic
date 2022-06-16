import React, { useState } from 'react'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Error from '../components/Error'
import Loading from '../components/Loading'
import PostThumb from '../components/PostThumb'
import { getDiscoverPosts } from '../_actions/PostActions'

function DiscoverScreen() {

    const dispatch = useDispatch()

    const auth = useSelector(state => state.auth)
    const discover = useSelector(state => state.discover)

    const [page, setPage] = useState(2)

    useEffect(() => {
        if(auth.user && auth.user.id){
            dispatch(getDiscoverPosts(1, auth.user.id))
        }
    }, [auth.user, auth.user.id])

    const handleLoadMore = () =>{
        dispatch(getDiscoverPosts(page, auth.user.id))
        setPage(page+1)
    }
    
    return (
        <div className="discover">
            <div style={{fontSize:'2.4rem', fontWeight:'800',textAlign:'center' , marginTop:'2rem', marginBottom:"2rem"}}>
                Find Co-Earthsavers!
            </div>
            {
                discover.error && discover.error.message && <Error error = {discover.error.message}/>
            }
            {
                discover.Loading && <Loading/>
            }
            {
                discover.posts &&
                <PostThumb posts ={discover.posts}/>
            }

            {
                discover.totalPage > page &&
                <button onClick={handleLoadMore}>Load More</button>
            }
        </div>
    )
}

export default DiscoverScreen
