import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { levelDescriptionIconShow } from '../utils/MediaShow'


function Level() {

    const [levels, setLevels] = useState([])

    const getlevels = async() =>{
        await axios.get("/levels").then(res=>
            res.data.forEach(res=>
                {
                    const level = {name:res.name, min:res.min, max:res.max}
                    levels.push(level)
                }
            )
        ).catch(err=>{
            console.log(err)
        })
    }

    useEffect(() => {
        getlevels()
    }, [])

    return (
        <div className="d-flex justify-content-center align-items-center" >
            {
                levels.map((level, index)=>(
                    <div key={index} className="d-flex align-items-center">
                        <div style={{textAlign:'center'}}>
                            {
                                levelDescriptionIconShow(level.name)
                            }

                            <div style={{fontSize:'1.2rem', fontWeight:"800" }}>
                                {level.name}    
                            </div>
                            <div style={{fontWeight:"700", color:"#b0b0b0"}}>
                                {
                                    level.min===0
                                    ? 'New Earth Saver'
                                    : `More than ${level.min} points`
                                }

                            </div>
                        </div>
                        <div>
                            {
                                index!==levels.length-1 &&
                                <i className="fas fa-arrow-right"></i>
                            }
                        </div>
                    </div>
                ))
            }
        </div>
    )
}

export default Level
