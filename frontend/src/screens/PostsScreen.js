import React, { useEffect, useState } from 'react'
import { useRef } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Error from '../components/Error'
import Loading from '../components/Loading'
import PostThumb from '../components/PostThumb'
import { getHomePost, getSearchPosts } from '../_actions/PostActions'

function PostsScreen(props) {

    const keyword = props.match.params.keyword

    const home = useSelector(state => state.home)

    const [page, setPage] = useState(1)
    const [isLoadMore, setIsLoadMore] = useState(false)
    const [sort, setSort] = useState('popular')

    const dispatch = useDispatch()
    const pageEnd = useRef()


    useEffect(() => {
        setPage(1)
        setIsLoadMore(false)
        if(keyword){
            dispatch(getSearchPosts(1, sort, keyword)) 
        }else{
            dispatch(getHomePost(1, sort))
        }

    }, [dispatch, keyword, sort])


    useEffect(() => {
        const observer = new IntersectionObserver(entries=>{
            if(entries[0].isIntersecting){
                setIsLoadMore(true)
            }
        },{
            threshold:0.1
        })
            observer.observe(pageEnd.current)
    }, [])

    useEffect(() => {
        if(isLoadMore && home.totalPage>page){
            handleLoadMore()
        }
        setIsLoadMore(false)

    }, [isLoadMore, dispatch])



    const handleLoadMore = ()=>{
        if(keyword){
            dispatch(getSearchPosts(page+1, sort, keyword))
            setPage(page=>page+1)

        }else{
            console.log("trigger")
            dispatch(getHomePost(page+1, sort))
            setPage(page=>page+1)
        }
    }
    
    return (
        <>
        <div className="product_sort">
            <span>SORT BY : </span>
            <select value={sort} onChange={e=>setSort(e.target.value)}>
                <option value="newest">Newest</option>
                <option value="popular">Most Popular</option>
            </select>
        </div>
        {
            home.error &&home.error.message && <Error error = {home.error.message}/>
        }
        {
            home.Loading && <Loading/>
        }

        {
            (home.loading===false && home.posts) &&
            <div className="post_screen">
            {
                home.posts &&
                <PostThumb posts ={home.posts}/>
            }
            </div>

        }
        
        <button style={{marginBottom:'100px', opacity:'0'}} ref={pageEnd}>loadmore</button>
        
        </>
    )
}

export default PostsScreen
