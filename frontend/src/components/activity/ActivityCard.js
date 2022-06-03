import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Link } from 'react-router-dom'
import earth from '../../images/earth.svg'
import { deleteActivity } from '../../_actions/ActivityActions'
import EditActivity from './EditActivity'

function ActivityCard({activity}) {

    const auth = useSelector(state => state.auth)
    const [isEdit, setIsEdit] = useState(false)

    const dispatch = useDispatch()

    const handleDelete = () =>{
        if(window.confirm('Are you sure to delete this activity?')){
            dispatch(deleteActivity(activity.id))
        }
    }
    return (
        <>
                <div className="activitycard">
                    {
                        auth && auth.user && auth.user.role === "ADMIN" &&
                        <div style={{textAlign:"right", marginBottom:"1rem"}}>
                            <button onClick={()=>setIsEdit(!isEdit)} style={{marginRight:"1rem"}}>Edit</button>
                            <button onClick={handleDelete}>Delete</button>
                        </div>
                    }
                    <div style={{marginBottom:"1rem"}}>
                        <Link to={`/activity/${activity.id}`} style={{display:"flex"}}>
                            <div className="activity_image">
                                <img src={activity.imageUrl?activity.imageUrl : earth} alt="activityImage"/>
                            </div>
                            <div className="activity_info">
                                <div className="activity_name">{activity.name}</div>
                                <div className="activity_score"><strong>{activity.score}</strong> points</div>
                                <div className="activity_description">{activity.description}</div>
                            </div>
                        </Link>
                    </div>

                </div>
            {
                isEdit &&
                <EditActivity activity={activity} setIsEdit={setIsEdit}/>
            }
        </>
    )
}

export default ActivityCard
